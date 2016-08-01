package com.engineerbetter.converger.facade;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.engineerbetter.converger.intents.CfUserIntent;
import com.engineerbetter.converger.intents.UaaUserIntent;
import com.engineerbetter.converger.properties.UaaUserProperties;
import com.engineerbetter.converger.resolution.IdentifiableResolution;

public class CfFacadeCfUserHandlerTest
{
	private CloudFoundryFacade cf;
	private UaaUserIntent uaaUserIntent;
	private CfUserIntent intent;
	private CfFacadeCfUserHandler handler;


	@Before
	public void setup()
	{
		cf = mock(CloudFoundryFacade.class);
		uaaUserIntent = new UaaUserIntent(new UaaUserProperties("test@example.com", "Geoff", "McGeofferson"));
		intent = new CfUserIntent(uaaUserIntent);
		handler = new CfFacadeCfUserHandler(intent, cf);
	}


	@Test
	public void absentIfUaaUserAbsent()
	{
		uaaUserIntent.setResolution(IdentifiableResolution.absent());
		handler.resolve();
		assertThat(intent.getResolution().exists(), is(false));
	}


	@Test
	public void presentIfFacadeReturnsTrue()
	{
		uaaUserIntent.setResolution(IdentifiableResolution.of("user-id"));
		given(cf.userExists("user-id")).willReturn(true);
		handler.resolve();
		assertThat(intent.getResolution().getId().get(), is("user-id"));
	}


	@Test
	public void absentIfFacadeReturnsFalse()
	{
		uaaUserIntent.setResolution(IdentifiableResolution.of("user-id"));
		given(cf.userExists("user-id")).willReturn(false);
		handler.resolve();
		assertThat(intent.getResolution().exists(), is(false));
	}


	@Test
	public void convergeNoopsIfPresent()
	{
		intent.setResolution(IdentifiableResolution.of("user-id"));
		handler.converge();
		then(cf).should(times(0)).createUser(any());
	}


	@Test
	public void convergeCreatesIfAbsent()
	{
		uaaUserIntent.setResolution(IdentifiableResolution.of("user-id"));
		intent.setResolution(IdentifiableResolution.absent());
		handler.converge();
		then(cf).should().createUser("user-id");
	}
}
