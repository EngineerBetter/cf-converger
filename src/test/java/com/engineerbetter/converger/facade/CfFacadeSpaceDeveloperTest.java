package com.engineerbetter.converger.facade;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.engineerbetter.converger.facade.CloudFoundryFacade.SpaceRole;
import com.engineerbetter.converger.intents.CfUserIntent;
import com.engineerbetter.converger.intents.OrgIntent;
import com.engineerbetter.converger.intents.SpaceDeveloperIntent;
import com.engineerbetter.converger.intents.SpaceIntent;
import com.engineerbetter.converger.intents.UaaUserIntent;
import com.engineerbetter.converger.properties.NameProperty;
import com.engineerbetter.converger.resolution.IdentifiableResolution;
import com.engineerbetter.converger.resolution.RelationshipResolution;

public class CfFacadeSpaceDeveloperTest
{
	private CloudFoundryFacade cf;
	private SpaceIntent spaceIntent;
	private CfUserIntent cfUserIntent;
	private SpaceDeveloperIntent intent;
	private CfFacadeSpaceDeveloperHandler handler;


	@Before
	public void setup()
	{
		cf = mock(CloudFoundryFacade.class);
		spaceIntent = new SpaceIntent(new NameProperty("DEV"), mock(OrgIntent.class));
		cfUserIntent = new CfUserIntent(mock(UaaUserIntent.class));
		intent = new SpaceDeveloperIntent(spaceIntent, cfUserIntent);
		handler = new CfFacadeSpaceDeveloperHandler(intent, cf);
	}


	@Test
	public void absentIfSpaceAbsent()
	{
		spaceIntent.setResolution(IdentifiableResolution.absent());
		cfUserIntent.setResolution(IdentifiableResolution.of("user-id"));
		handler.resolve();
		assertThat(intent.getResolution().exists(), is(false));
	}


	@Test
	public void absentIfUserAbsent()
	{
		spaceIntent.setResolution(IdentifiableResolution.of("space-id"));
		cfUserIntent.setResolution(IdentifiableResolution.absent());
		handler.resolve();
		assertThat(intent.getResolution().exists(), is(false));
	}


	@Test
	public void absentIfFacadeReturnsFalse()
	{
		spaceIntent.setResolution(IdentifiableResolution.of("space-id"));
		cfUserIntent.setResolution(IdentifiableResolution.of("user-id"));
		given(cf.hasSpaceRole("user-id", "space-id", SpaceRole.DEVELOPER)).willReturn(false);
		handler.resolve();
		assertThat(intent.getResolution().exists(), is(false));
	}


	@Test
	public void presentIfFacadeReturnsTrue()
	{
		spaceIntent.setResolution(IdentifiableResolution.of("space-id"));
		cfUserIntent.setResolution(IdentifiableResolution.of("user-id"));
		given(cf.hasSpaceRole("user-id", "space-id", SpaceRole.DEVELOPER)).willReturn(true);
		handler.resolve();
		assertThat(intent.getResolution().exists(), is(true));
	}


	@Test
	public void convergeNoopsIfPresent()
	{
		intent.setResolution(RelationshipResolution.of(true));
		handler.converge();
		then(cf).should(times(0)).setSpaceRole(any(), any(), any());
	}


	@Test
	public void convergeCreatesIfAbsent()
	{
		spaceIntent.setResolution(IdentifiableResolution.of("space-id"));
		cfUserIntent.setResolution(IdentifiableResolution.of("user-id"));
		intent.setResolution(RelationshipResolution.of(false));
		handler.converge();
		then(cf).should().setSpaceRole("user-id", "space-id", SpaceRole.DEVELOPER);
	}
}
