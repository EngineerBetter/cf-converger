package com.engineerbetter.converger.resolution;

import java.util.Optional;

import com.engineerbetter.converger.intents.Intent;

class ConcreteIdentifiableResolution implements IdentifiableResolution
{
	private final Optional<String> id;

	protected ConcreteIdentifiableResolution(Optional<String> id)
	{
		if(id != null)
		{
			this.id = id;
		}
		else
		{
			this.id = Optional.empty();
		}
	}


	@Override
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
	public String convergenceDescription(Intent<? extends Resolution> intent)
	{
		if(exists())
		{
			return "Would not create "+intent;
		}
		else
		{
			return "Would create "+intent;
		}
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
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}
}