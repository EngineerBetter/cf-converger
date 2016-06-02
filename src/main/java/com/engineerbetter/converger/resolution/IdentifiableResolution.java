package com.engineerbetter.converger.resolution;

import java.util.Optional;

public interface IdentifiableResolution extends Resolution
{
	Optional<String> getId();


	public static IdentifiableResolution of(Optional<String> id)
	{
		return new IdentifiableResolution() {

			@Override
			public boolean exists()
			{
				return id.isPresent();
			}

			@Override
			public Optional<String> getId()
			{
				return id;
			}
		};
	}


	public static IdentifiableResolution absent()
	{
		return new IdentifiableResolution() {

			@Override
			public boolean exists()
			{
				return false;
			}

			@Override
			public Optional<String> getId()
			{
				return Optional.empty();
			}
		};
	}
}
