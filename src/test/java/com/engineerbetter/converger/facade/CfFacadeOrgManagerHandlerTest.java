package com.engineerbetter.converger.facade;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.engineerbetter.converger.facade.CloudFoundryFacade.OrgRole;
import com.engineerbetter.converger.intents.CfUserIntent;
import com.engineerbetter.converger.intents.OrgIntent;
import com.engineerbetter.converger.intents.OrgManagerIntent;
import com.engineerbetter.converger.intents.UaaUserIntent;
import com.engineerbetter.converger.properties.NameProperty;
import com.engineerbetter.converger.properties.UaaUserProperties;
import com.engineerbetter.converger.resolution.IdentifiableResolution;
import com.engineerbetter.converger.resolution.RelationshipResolution;

public class CfFacadeOrgManagerHandlerTest
{
	private CloudFoundryFacade cf;
	private OrgIntent orgIntent;
	private UaaUserIntent uaaUserIntent;
	private CfUserIntent cfUserIntent;
	private OrgManagerIntent intent;
	private CfFacadeOrgManagerHandler handler;


	@Before
	public void setup()
	{
		cf = mock(CloudFoundryFacade.class);
		orgIntent = new OrgIntent(new NameProperty("my-org"));
		uaaUserIntent = new UaaUserIntent(new UaaUserProperties("test@example.com", "Geoff", "McGeofferson"));
		cfUserIntent = new CfUserIntent(uaaUserIntent);
		intent = new OrgManagerIntent(orgIntent, cfUserIntent);
		handler = new CfFacadeOrgManagerHandler(intent, cf);
	}


	@Test
	public void absentIfOrgAbsent()
	{
		orgIntent.setResolution(IdentifiableResolution.absent());
		cfUserIntent.setResolution(IdentifiableResolution.of("user-id"));
		handler.resolve();
		assertThat(intent.getResolution().exists(), is(false));
	}


	@Test
	public void absentIfUserAbsent()
	{
		orgIntent.setResolution(IdentifiableResolution.of("org-id"));
		cfUserIntent.setResolution(IdentifiableResolution.absent());
		handler.resolve();
		assertThat(intent.getResolution().exists(), is(false));
	}


	@Test
	public void existsIfFacadeSaysSo()
	{
		orgIntent.setResolution(IdentifiableResolution.of("org-id"));
		cfUserIntent.setResolution(IdentifiableResolution.of("user-id"));
		given(cf.hasOrgRole("user-id", "org-id", OrgRole.ORG_MANAGER)).willReturn(true);
		handler.resolve();
		assertThat(intent.getResolution().exists(), is(true));
	}


	@Test
	public void doesNotExistIfFacadeReturnsFalse()
	{
		orgIntent.setResolution(IdentifiableResolution.of("org-id"));
		cfUserIntent.setResolution(IdentifiableResolution.of("user-id"));
		given(cf.hasOrgRole("user-id", "org-id", OrgRole.ORG_MANAGER)).willReturn(false);
		handler.resolve();
		assertThat(intent.getResolution().exists(), is(false));
	}


	@Test
	public void convergesNoopsIfPresent()
	{
		intent.setResolution(RelationshipResolution.of(true));
		handler.converge();
		then(cf).should(times(0)).addUserToOrg("user-id", "org-id");
	}


	@Test
	public void convergesCreatesIfAbsent()
	{
		cfUserIntent.setResolution(IdentifiableResolution.of("user-id"));
		orgIntent.setResolution(IdentifiableResolution.of("org-id"));
		intent.setResolution(RelationshipResolution.of(false));
		handler.converge();
		then(cf).should().setOrgRole("user-id", "org-id",OrgRole.ORG_MANAGER);
	}
}
