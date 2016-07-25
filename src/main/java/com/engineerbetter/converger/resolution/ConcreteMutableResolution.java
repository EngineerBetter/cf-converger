package com.engineerbetter.converger.resolution;

import java.util.List;
import java.util.Optional;

import org.javers.core.diff.Change;
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.diff.changetype.map.EntryAdded;
import org.javers.core.diff.changetype.map.EntryChange;
import org.javers.core.diff.changetype.map.EntryRemoved;
import org.javers.core.diff.changetype.map.EntryValueChange;
import org.javers.core.diff.changetype.map.MapChange;

import com.engineerbetter.converger.intents.Intent;

class ConcreteMutableResolution extends ConcreteIdentifiableResolution implements MutableResolution
{
	private final Optional<List<Change>> differences;


	protected ConcreteMutableResolution()
	{
		super();
		this.differences = Optional.empty();
	}


	protected ConcreteMutableResolution(String id)
	{
		super(id);
		this.differences = Optional.empty();
	}


	protected ConcreteMutableResolution(Optional<String> id)
	{
		super(id);
		this.differences = Optional.empty();
	}


	protected ConcreteMutableResolution(String id, List<Change> differences)
	{
		this(Optional.of(id), Optional.of(differences));
	}

	protected ConcreteMutableResolution(Optional<String> id, Optional<List<Change>> differences)
	{
		super(id);

		if(differences != null && differences.isPresent() && differences.get().size() > 0)
		{
			this.differences = differences;
		}
		else
		{
			this.differences = Optional.empty();
		}
	}


	@Override
	public boolean hasDifferences()
	{
		return differences.isPresent();
	}


	@Override
	public Optional<List<Change>> getDifferences()
	{
		return differences;
	}


	// This is a bit odd, why do we need to pass an intent that is used only for the class name?
	@Override
	public String convergenceDescription(Intent<? extends Resolution> intent)
	{
		if(exists())
		{
			if(! hasDifferences())
			{
				return "Would not create "+intent;
			}
			else
			{
				StringBuilder builder = new StringBuilder();
				for(Change difference : differences.get())
				{
					if(difference instanceof ValueChange)
					{
						ValueChange valueDiff = (ValueChange) difference;
						builder.append("changing "+valueDiff.getPropertyName()+" from "+valueDiff.getLeft()+" to "+valueDiff.getRight());
					}
					else if (difference instanceof MapChange)
					{
						MapChange mapChange = (MapChange) difference;
						for(EntryChange entryChange : mapChange.getEntryChanges())
						{
							if(entryChange instanceof EntryAdded)
							{
								EntryAdded added = (EntryAdded) entryChange;
								builder.append("adding entry "+added.getKey()+"->"+added.getValue()+" to "+mapChange.getPropertyName());
							}
							else if(entryChange instanceof EntryRemoved)
							{
								EntryRemoved removed = (EntryRemoved) entryChange;
								builder.append("removing entry "+removed.getKey()+"->"+removed.getValue()+" from "+mapChange.getPropertyName());
							}
							else if(entryChange instanceof EntryValueChange)
							{
								EntryValueChange changed = (EntryValueChange) entryChange;
								builder.append("changing entry "+changed.getKey()+ " from "+changed.getLeftValue()+" to "+changed.getRightValue()+" in "+mapChange.getPropertyName());
							}
						}
					}
				}
				return "Would update "+intent.getClass().getSimpleName()+", "+builder.toString();
			}
		}
		else
		{
			return "Would create "+intent;
		}
	}
}
