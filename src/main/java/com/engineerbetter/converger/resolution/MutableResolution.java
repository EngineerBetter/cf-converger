package com.engineerbetter.converger.resolution;

import java.util.List;
import java.util.Optional;

import org.javers.core.diff.Change;

public interface MutableResolution extends IdentifiableResolution
{
	boolean hasDifferences();


	Optional<List<Change>> getDifferences();


	public static MutableResolution absent()
	{
		return new ConcreteMutableResolution(Optional.empty(), Optional.empty());
	}


	public static MutableResolution same(String id)
	{
		return new ConcreteMutableResolution(Optional.of(id), Optional.empty());
	}


	public static MutableResolution different(String id, List<Change> differences)
	{

		return new ConcreteMutableResolution(id, differences);
	}
}
