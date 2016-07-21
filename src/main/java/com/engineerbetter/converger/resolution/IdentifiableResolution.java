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
		if(id == null)
		{
			return absent();
		}
		else
		{
			return new ConcreteIdentifiableResolution(Optional.of(id));
		}
	}


	public static IdentifiableResolution absent()
	{
		return new ConcreteIdentifiableResolution(Optional.empty());
	}
}
