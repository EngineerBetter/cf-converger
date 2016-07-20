package com.engineerbetter.converger.resolution;

import java.util.Optional;

public abstract class MutableResolution extends IdentifiableResolution
{
	protected MutableResolution(Optional<String> id)
	{
		super(id);
	}

	public abstract Optional<Difference> getDifference();
}
