package com.engineerbetter.converger.facade;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.javers.core.diff.Change;
import org.javers.core.diff.changetype.map.EntryChange;
import org.javers.core.diff.changetype.map.EntryValueChange;
import org.javers.core.diff.changetype.map.MapChange;
import org.javers.core.metamodel.object.UnboundedValueObjectId;
import org.junit.Before;
import org.junit.Test;

import com.engineerbetter.converger.intents.SpaceIntent;
import com.engineerbetter.converger.intents.UpsIntent;
import com.engineerbetter.converger.properties.NameProperty;
import com.engineerbetter.converger.properties.UpsProperties;
import com.engineerbetter.converger.resolution.IdentifiableResolution;
import com.engineerbetter.converger.resolution.MutableResolution;

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
		assertThat(intent.getResolution(), is(MutableResolution.absent()));
	}

	@Test
	public void absentIfNoIdFound()
	{
		spaceIntent.setResolution(IdentifiableResolution.of("space-id"));
		when(cf.findUps("upsName", "space-id")).thenReturn(Optional.<String>empty());
		handler.resolve();
		assertThat(intent.getResolution(), is(MutableResolution.absent()));
	}

	@Test
	public void sameIfIdWithSamePropertiesFound()
	{
		spaceIntent.setResolution(IdentifiableResolution.of("space-id"));
		when(cf.findUps("upsName", "space-id")).thenReturn(Optional.<String>of("ups-id"));
		when(cf.getUps("ups-id")).thenReturn(intent.upsProperties);
		handler.resolve();
		assertThat(intent.getResolution(), is(MutableResolution.same("ups-id")));
	}

	@Test
	public void resolvedAsDifferent()
	{
		spaceIntent.setResolution(IdentifiableResolution.of("space-id"));

		Map<String, String> actualCredentials = new HashMap<>();
		actualCredentials.put("username", "admin");
		actualCredentials.put("password", "passw0rd");
		UpsProperties observed = new UpsProperties("upsName", actualCredentials);

		when(cf.findUps("upsName", "space-id")).thenReturn(Optional.<String>of("ups-id"));
		when(cf.getUps("ups-id")).thenReturn(observed);

		handler.resolve();

		List<Change> differences = new LinkedList<>();
		UnboundedValueObjectId affectedCdoId = new UnboundedValueObjectId(UpsProperties.class.getName());
		List<EntryChange> mapChanges = Arrays.asList(new EntryValueChange("password", "passw0rd", "tiger"), new EntryValueChange("username", "admin", "scott"));
		MapChange credentialsChange = new MapChange(affectedCdoId, "credentials", mapChanges);
		differences.add(credentialsChange);
		assertThat(intent.getResolution().getDifferences().get(), equalTo(differences));
	}


	@Test
	public void convergeCreatesWhenAbsent()
	{
		spaceIntent.setResolution(IdentifiableResolution.of("space-id"));
		intent.setResolution(MutableResolution.absent());
		when(cf.createUps(intent.upsProperties, "space-id")).thenReturn("new-ups-id");
		handler.converge();
		assertThat(intent.getResolution().hasDifferences(), is(false));
		assertThat(intent.getResolution().getId().get(), equalTo("new-ups-id"));
	}


	@Test
	public void convergeNoopsWhenPresent()
	{
		spaceIntent.setResolution(IdentifiableResolution.of("space-id"));
		intent.setResolution(MutableResolution.same("ups-id"));
		handler.converge();
		verify(cf, times(0)).createUps(any(), any());
	}


	@Test
	public void convergeUpdatesWhenDifferent()
	{
		spaceIntent.setResolution(IdentifiableResolution.of("space-id"));
		Map<String, String> actualCredentials = new HashMap<>();
		actualCredentials.put("username", "admin");
		actualCredentials.put("password", "passw0rd");
		UpsProperties observed = new UpsProperties("upsName", actualCredentials);

		given(cf.findUps("upsName", "space-id")).willReturn(Optional.<String>of("ups-id"));
		given(cf.getUps("ups-id")).willReturn(observed);

		handler.resolve();
		handler.converge();

		then(cf).should().updateUps(intent.upsProperties, "space-id");
	}
}
