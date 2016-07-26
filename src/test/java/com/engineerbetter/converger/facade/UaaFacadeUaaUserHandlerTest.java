package com.engineerbetter.converger.facade;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.engineerbetter.converger.intents.UaaUserIntent;
import com.engineerbetter.converger.properties.UaaUserProperties;
import com.engineerbetter.converger.resolution.IdentifiableResolution;

public class UaaFacadeUaaUserHandlerTest
{
	private UaaFacade uaa;
	private UaaUserIntent intent;
	private UaaFacadeUaaUserHandler handler;

	@Before
	public void setup()
	{
		uaa = mock(UaaFacade.class);
		UaaUserProperties properties = new UaaUserProperties("test@example.com", "Geoff", "McGeofferson");
		intent = new UaaUserIntent(properties);
		handler = new UaaFacadeUaaUserHandler(intent, uaa);
	}


	@Test
	public void absentIfNoIdFound()
	{
		given(uaa.findUser("test@example.com")).willReturn(Optional.empty());
		handler.resolve();
		assertThat(intent.getResolution(), is(IdentifiableResolution.absent()));
	}


	@Test
	public void existsIfFound()
	{
		given(uaa.findUser("test@example.com")).willReturn(Optional.of("uaa-id"));
		handler.resolve();
		assertThat(intent.getResolution(), is(IdentifiableResolution.of("uaa-id")));
	}


	@Test
	public void convergeCreatesIfAbsent()
	{
		intent.setResolution(IdentifiableResolution.absent());
		given(uaa.createUser(intent.properties)).willReturn("new-id");
		handler.converge();
		verify(uaa).createUser(new UaaUserProperties("test@example.com", "Geoff", "McGeofferson"));
		assertThat(intent.getResolution().exists(), is(true));
	}


	@Test
	public void convergeNoopsIfSame()
	{
		intent.setResolution(IdentifiableResolution.of("ups-id"));
		handler.converge();
		verify(uaa, times(0)).createUser(any());
		assertThat(intent.getResolution().exists(), is(true));
	}
}
