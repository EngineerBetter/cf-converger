package com.engineerbetter.converger.facade;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.engineerbetter.converger.intents.CfUserIntent;
import com.engineerbetter.converger.intents.OrgIntent;
import com.engineerbetter.converger.intents.UaaUserIntent;
import com.engineerbetter.converger.intents.UserOrgIntent;
import com.engineerbetter.converger.properties.NameProperty;
import com.engineerbetter.converger.properties.UaaUserProperties;
import com.engineerbetter.converger.resolution.IdentifiableResolution;
import com.engineerbetter.converger.resolution.RelationshipResolution;

public class CfFacadeUserOrgHandlerTest
{
	private CloudFoundryFacade cf;
	private UaaUserIntent uaaUserIntent;
	private CfUserIntent cfUserIntent;
	private OrgIntent orgIntent;
	private UserOrgIntent intent;
	private CfFacadeUserOrgHandler handler;


	@Before
	public void setup()
	{
		cf = mock(CloudFoundryFacade.class);
		uaaUserIntent = new UaaUserIntent(new UaaUserProperties("test@example.com", "Geoff", "McGeofferson"));
		cfUserIntent = new CfUserIntent(uaaUserIntent);
		orgIntent = new OrgIntent(new NameProperty("my-org"));
		intent = new UserOrgIntent(orgIntent, cfUserIntent);
		handler = new CfFacadeUserOrgHandler(intent, cf);
	}


	@Test
	public void absentIfUserAbsent()
	{
		cfUserIntent.setResolution(IdentifiableResolution.absent());
		orgIntent.setResolution(IdentifiableResolution.of("org-id"));
		handler.resolve();
		assertThat(intent.getResolution().exists(), is(false));
	}

	@Test
	public void absentIfOrgAbsent()
	{
		cfUserIntent.setResolution(IdentifiableResolution.of("user-id"));
		orgIntent.setResolution(IdentifiableResolution.absent());
		handler.resolve();
		assertThat(intent.getResolution().exists(), is(false));
	}


	@Test
	public void absentIfFacadeReturnsFalse()
	{
		cfUserIntent.setResolution(IdentifiableResolution.of("user-id"));
		orgIntent.setResolution(IdentifiableResolution.of("org-id"));
		given(cf.isUserInOrg("user-id", "org-id")).willReturn(false);
		handler.resolve();
		assertThat(intent.getResolution().exists(), is(false));
	}


	@Test
	public void presentIfFacadeReturnsTrue()
	{
		cfUserIntent.setResolution(IdentifiableResolution.of("user-id"));
		orgIntent.setResolution(IdentifiableResolution.of("org-id"));
		given(cf.isUserInOrg("user-id", "org-id")).willReturn(true);
		handler.resolve();
		assertThat(intent.getResolution().exists(), is(true));
	}


	@Test
	public void convergeNoopsIfPresent()
	{
		intent.setResolution(RelationshipResolution.of(true));
		handler.converge();
		then(cf).should(times(0)).addUserToOrg(any(), any());
	}


	@Test
	public void convergeCreatesIfAbsent()
	{
		cfUserIntent.setResolution(IdentifiableResolution.of("user-id"));
		orgIntent.setResolution(IdentifiableResolution.of("org-id"));
		intent.setResolution(RelationshipResolution.of(false));
		handler.converge();
		then(cf).should().addUserToOrg("user-id", "org-id");
	}
}
