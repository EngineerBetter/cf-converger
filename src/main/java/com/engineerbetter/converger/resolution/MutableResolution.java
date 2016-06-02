package com.engineerbetter.converger.resolution;

import java.util.Optional;

public interface MutableResolution extends IdentifiableResolution
{
	Optional<Difference> getDifference();
}
