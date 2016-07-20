package com.engineerbetter.converger.resolution;

import java.util.Optional;

public class IdentifiableResolution implements Resolution
{
	private final Optional<String> id;

	protected IdentifiableResolution(Optional<String> id)
	{
		this.id = id;
	}

	public Optional<String> getId()
	{
		return id;
	}

	@Override
	public boolean exists()
	{
		return id.isPresent();
	}


	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IdentifiableResolution other = (IdentifiableResolution) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	public static IdentifiableResolution of(Optional<String> id)
	{
		return new IdentifiableResolution(id);
	}


	public static IdentifiableResolution absent()
	{
		return new IdentifiableResolution(Optional.empty());
	}
}
