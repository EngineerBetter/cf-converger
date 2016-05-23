package com.engineerbetter.converger.intents;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.engineerbetter.converger.facade.UaaFacade;

public class UaaUserIntent implements IdentifiableIntent
{
	public final String name;
	private Optional<String> id = Optional.empty();
	@Autowired
	private UaaFacade uaa;

	public UaaUserIntent(String name)
	{
		this.name = name;
	}

	@Override
	public void resolve()
	{
		id = uaa.findUser(name);
	}

	@Override
	public Optional<String> id()
	{
		return id;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		UaaUserIntent other = (UaaUserIntent) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null)
		{
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
