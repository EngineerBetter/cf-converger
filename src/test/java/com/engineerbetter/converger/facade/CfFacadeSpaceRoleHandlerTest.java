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
import com.engineerbetter.converger.intents.Handler;
import com.engineerbetter.converger.intents.Intent;
import com.engineerbetter.converger.intents.OrgIntent;
import com.engineerbetter.converger.intents.SpaceIntent;
import com.engineerbetter.converger.intents.UaaUserIntent;
import com.engineerbetter.converger.properties.NameProperty;
import com.engineerbetter.converger.resolution.IdentifiableResolution;
import com.engineerbetter.converger.resolution.RelationshipResolution;

public abstract class CfFacadeSpaceRoleHandlerTest<I extends Intent<RelationshipResolution>, H extends Handler<I>>
{
	protected CloudFoundryFacade cf;
	protected SpaceIntent spaceIntent;
	protected CfUserIntent cfUserIntent;
	protected I intent;
	protected H handler;


	@Before
	public void setup()
	{
		cf = mock(CloudFoundryFacade.class);
		spaceIntent = new SpaceIntent(new NameProperty("DEV"), mock(OrgIntent.class));
		cfUserIntent = new CfUserIntent(mock(UaaUserIntent.class));
		setupInstances();
	}


	abstract void setupInstances();


	abstract SpaceRole getRole();


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
		given(cf.hasSpaceRole("user-id", "space-id", getRole())).willReturn(false);
		handler.resolve();
		assertThat(intent.getResolution().exists(), is(false));
	}

	@Test
	public void presentIfFacadeReturnsTrue()
	{
		spaceIntent.setResolution(IdentifiableResolution.of("space-id"));
		cfUserIntent.setResolution(IdentifiableResolution.of("user-id"));
		given(cf.hasSpaceRole("user-id", "space-id", getRole())).willReturn(true);
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
		then(cf).should().setSpaceRole("user-id", "space-id", getRole());
	}

}
