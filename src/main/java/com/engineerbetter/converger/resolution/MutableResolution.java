package com.engineerbetter.converger.resolution;

import java.util.Optional;

public interface MutableResolution extends IdentifiableResolution
{
	Optional<Difference> getDifference();

	public static MutableResolution absent()
	{
		return new ConcreteMutableResolution(Optional.empty(), Optional.empty());
	}


	public static MutableResolution same(String id)
	{
		return new ConcreteMutableResolution(Optional.of(id), Optional.empty());
	}
}
