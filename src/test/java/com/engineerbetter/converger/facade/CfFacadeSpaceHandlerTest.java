package com.engineerbetter.converger.facade;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.engineerbetter.converger.intents.OrgIntent;
import com.engineerbetter.converger.intents.SpaceIntent;
import com.engineerbetter.converger.properties.NameProperty;
import com.engineerbetter.converger.resolution.IdentifiableResolution;

public class CfFacadeSpaceHandlerTest
{
	private CloudFoundryFacade cf;
	private OrgIntent orgIntent;
	private SpaceIntent intent;
	private CfFacadeSpaceHandler handler;

	@Before
	public void setup()
	{
		cf = mock(CloudFoundryFacade.class);
		orgIntent = new OrgIntent(new NameProperty("my-org"));
		orgIntent.setResolution(IdentifiableResolution.of("org-id"));
		intent = new SpaceIntent(new NameProperty("dev"), orgIntent);
		handler = new CfFacadeSpaceHandler(intent, cf);
	}

	@Test
	public void resolveCallsFacade()
	{
		when(cf.findSpace("dev", "org-id")).thenReturn(Optional.<String>of("space-id"));
		handler.resolve();
		assertThat(intent.getResolution(), is(IdentifiableResolution.of("space-id")));
	}

	@Test
	public void createsWhenAbsent()
	{
		intent.setResolution(IdentifiableResolution.absent());
		when(cf.createSpace("dev", "org-id")).thenReturn("space-id");
		handler.converge();
		assertThat(intent.getResolution().getId().get(), is("space-id"));
	}


	@Test
	public void noopWhenPresent()
	{
		intent.setResolution(IdentifiableResolution.of(Optional.<String>of("some-id")));
		handler.converge();
		verify(cf, times(0)).createSpace(anyString(), anyString());
	}
}
