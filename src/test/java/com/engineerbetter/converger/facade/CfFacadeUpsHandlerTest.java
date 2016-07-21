package com.engineerbetter.converger.facade;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.engineerbetter.converger.intents.SpaceIntent;
import com.engineerbetter.converger.intents.UpsIntent;
import com.engineerbetter.converger.properties.NameProperty;
import com.engineerbetter.converger.properties.UpsProperties;
import com.engineerbetter.converger.resolution.IdentifiableResolution;

public class CfFacadeUpsHandlerTest
{
	private CloudFoundryFacade cf;
	private UpsIntent intent;
	private CfFacadeUpsHandler handler;
	private SpaceIntent spaceIntent;
	private Map<String, String> upsCredentials;

	@Before
	public void setup()
	{
		cf = mock(CloudFoundryFacade.class);
		upsCredentials = new HashMap<>();
		upsCredentials.put("username", "scott");
		upsCredentials.put("password", "tiger");
		UpsProperties properties = new UpsProperties("upsName", upsCredentials);
		spaceIntent = new SpaceIntent(new NameProperty("name"), null);
		intent = new UpsIntent(properties, spaceIntent);
		handler = new CfFacadeUpsHandler(intent, cf);
	}

	@Test
	public void absentIfSpaceAbsent()
	{
		spaceIntent.setResolution(IdentifiableResolution.absent());
		handler.resolve();
		assertThat(intent.getResolution(), is(IdentifiableResolution.absent()));
	}

	@Test
	public void resolvedIfIdFound()
	{
		spaceIntent.setResolution(IdentifiableResolution.of("space-id"));
		when(cf.findUps("upsName", "space-id")).thenReturn(Optional.<String>of("ups-id"));
		handler.resolve();
		assertThat(intent.getResolution(), is(IdentifiableResolution.of("ups-id")));
	}

	@Test
	public void absentIfNoIdFound()
	{
		spaceIntent.setResolution(IdentifiableResolution.of("space-id"));
		when(cf.findUps("upsName", "space-id")).thenReturn(Optional.<String>empty());
		handler.resolve();
		assertThat(intent.getResolution(), is(IdentifiableResolution.absent()));
	}


	@Test
	public void convergeCreatesWhenAbsent()
	{
		spaceIntent.setResolution(IdentifiableResolution.of("space-id"));
		intent.setResolution(IdentifiableResolution.absent());
		handler.converge();
		verify(cf).createUps("upsName", upsCredentials, "space-id");
	}


	@Test public void convergeNoopsWhenPresent()
	{
		spaceIntent.setResolution(IdentifiableResolution.of("space-id"));
		intent.setResolution(IdentifiableResolution.of("ups-id"));
		handler.converge();
		verify(cf, times(0)).createUps(any(), any(), any());
	}
}
