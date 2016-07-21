package com.engineerbetter.converger.resolution;

import java.util.Optional;

class ConcreteMutableResolution extends ConcreteIdentifiableResolution implements MutableResolution
{
	private final Optional<Difference> difference;

	protected ConcreteMutableResolution(Optional<String> id, Optional<Difference> difference)
	{
		super(id);

		if(difference != null)
		{
			this.difference = difference;
		}
		else
		{
			this.difference = Optional.empty();
		}
	}


	@Override
	public Optional<Difference> getDifference()
	{
		return difference;
	}
}
