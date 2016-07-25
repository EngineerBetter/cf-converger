package com.engineerbetter.converger.resolution;

import java.util.Optional;

public interface IdentifiableResolution extends Resolution
{
	Optional<String> getId();


	public static IdentifiableResolution of(Optional<String> id)
	{
		return new ConcreteIdentifiableResolution(id);
	}


	public static IdentifiableResolution of(String id)
	{
		return new ConcreteIdentifiableResolution(id);
	}


	public static IdentifiableResolution absent()
	{
		return new ConcreteIdentifiableResolution();
	}
}
