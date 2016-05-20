package com.engineerbetter.conveger.model;

import java.util.Optional;
import java.util.UUID;

public class SpaceIntent implements Intent
{
	public final String name;
	private Optional<UUID> id;

	public SpaceIntent(String name)
	{
		this.name = name;
	}

	@Override
	public void resolve()
	{
		//Look up in CloudController
		this.id = Optional.of(UUID.randomUUID());
	}

	@Override
	public Optional<UUID> resolved()
	{
		return this.id;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		SpaceIntent other = (SpaceIntent) obj;
		if (name == null)
		{
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "SpaceIntent [name=" + name + "]";
	}

}
