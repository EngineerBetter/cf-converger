package com.engineerbetter.converger.resolution;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.List;

import org.javers.core.diff.Change;
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.diff.changetype.map.EntryAdded;
import org.javers.core.diff.changetype.map.EntryChange;
import org.javers.core.diff.changetype.map.EntryRemoved;
import org.javers.core.diff.changetype.map.EntryValueChange;
import org.javers.core.diff.changetype.map.MapChange;
import org.javers.core.metamodel.object.ValueObjectId;
import org.junit.Test;

import com.engineerbetter.converger.intents.Intent;

public class MutableResolutionTest
{
	@Test
	public void absent()
	{
		MutableResolution resolution = MutableResolution.absent();
		assertThat(resolution.exists(), is(false));
		assertThat(resolution.getDifferences().isPresent(), is(false));
	}


	@Test
	public void same()
	{
		MutableResolution resolution = MutableResolution.same("id");
		assertThat(resolution.exists(), is(true));
		assertThat(resolution.getId().get(), equalTo("id"));
		assertThat(resolution.getDifferences().isPresent(), is(false));
	}


	@Test
	public void different()
	{
		List<Change> differences = new LinkedList<>();
		differences.add(new ValueChange(mock(ValueObjectId.class), "name", "oldName", "newName"));
		MutableResolution resolution = MutableResolution.different("id", differences);
		assertThat(resolution.exists(), is(true));
		assertThat(resolution.getId().get(), is("id"));
		assertThat(resolution.hasDifferences(), is(true));
		assertThat(resolution.getDifferences().get(), equalTo(differences));
	}


	@Test
	public void valueChangeIsDescribed()
	{
		List<Change> differences = new LinkedList<>();
		differences.add(new ValueChange(mock(ValueObjectId.class), "name", "oldName", "newName"));
		MutableResolution resolution = MutableResolution.different("id", differences);
		assertThat(resolution.convergenceDescription(new TestIntent()), is("Would update TestIntent, changing name from oldName to newName"));
	}


	@Test
	public void mapEntryAddedIsDescribed()
	{
		List<Change> differences = new LinkedList<>();
		List<EntryChange> entryChanges = new LinkedList<>();
		entryChanges.add(new EntryAdded("newfield", "newvalue"));
		differences.add(new MapChange(mock(ValueObjectId.class), "credentials", entryChanges));
		MutableResolution resolution = MutableResolution.different("id", differences);
		assertThat(resolution.convergenceDescription(new TestIntent()), is("Would update TestIntent, adding entry newfield->newvalue to credentials"));
	}


	@Test
	public void mapEntryRemovedIsDescribed()
	{
		List<Change> differences = new LinkedList<>();
		List<EntryChange> entryChanges = new LinkedList<>();
		entryChanges.add(new EntryRemoved("secret", "shhh"));
		differences.add(new MapChange(mock(ValueObjectId.class), "credentials", entryChanges));
		MutableResolution resolution = MutableResolution.different("id", differences);
		assertThat(resolution.convergenceDescription(new TestIntent()), is("Would update TestIntent, removing entry secret->shhh from credentials"));
	}


	@Test
	public void mapEntryChangedIsDescribed()
	{
		List<Change> differences = new LinkedList<>();
		List<EntryChange> entryChanges = new LinkedList<>();
		entryChanges.add(new EntryValueChange("password", "oldPassword", "newPassword"));
		differences.add(new MapChange(mock(ValueObjectId.class), "credentials", entryChanges));
		MutableResolution resolution = MutableResolution.different("id", differences);
		assertThat(resolution.convergenceDescription(new TestIntent()), is("Would update TestIntent, changing entry password from oldPassword to newPassword in credentials"));
	}


	public static final class TestIntent implements Intent<MutableResolution>
	{
		private MutableResolution resolution;

		@Override
		public MutableResolution getResolution()
		{
			return resolution;
		}

		@Override
		public void setResolution(MutableResolution resolution)
		{
			this.resolution = resolution;
		}

	}
}
