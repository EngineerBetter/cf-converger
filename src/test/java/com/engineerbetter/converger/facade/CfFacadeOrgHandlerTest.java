package com.engineerbetter.converger.facade;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.engineerbetter.converger.intents.OrgIntent;
import com.engineerbetter.converger.properties.NameProperty;
import com.engineerbetter.converger.resolution.IdentifiableResolution;

public class CfFacadeOrgHandlerTest
{
	private CloudFoundryFacade cf;
	private OrgIntent intent;
	private CfFacadeOrgHandler handler;

	@Before
	public void setup()
	{
		cf = mock(CloudFoundryFacade.class);
		intent = new OrgIntent(new NameProperty("my-org"));
		handler = new CfFacadeOrgHandler(intent, cf);
	}


	@Test
	public void resolveCallsFacade()
	{
		when(cf.findOrg("my-org")).thenReturn(Optional.<String>empty());
		handler.resolve();
		assertThat(intent.getResolution(), is(IdentifiableResolution.absent()));
	}


	@Test
	public void createsWhenAbsent()
	{
		intent.setResolution(IdentifiableResolution.absent());
		handler.converge();
		verify(cf).createOrg("my-org");
	}


	@Test
	public void noopWhenPresent()
	{
		intent.setResolution(IdentifiableResolution.of(Optional.<String>of("some-id")));
		handler.converge();
		verify(cf, times(0)).createOrg("my-org");
	}
}
