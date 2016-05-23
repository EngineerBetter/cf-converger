package com.engineerbetter.converger.intents;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.engineerbetter.converger.facade.CloudFoundryFacade;

public class CfUserIntent implements IdentifiableIntent
{
	private final UaaUserIntent uaaUserIntent;
	private Optional<String> id;
	@Autowired
	private CloudFoundryFacade cf;


	public CfUserIntent(UaaUserIntent uaaUserIntent)
	{
		this.uaaUserIntent = uaaUserIntent;
	}

	@Override
	public void resolve()
	{
		if(uaaUserIntent.id().isPresent() && cf.userExists(uaaUserIntent.id().get()))
		{
			id = uaaUserIntent.id();
		}
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
		result = prime * result
				+ ((uaaUserIntent == null) ? 0 : uaaUserIntent.hashCode());
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
		CfUserIntent other = (CfUserIntent) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (uaaUserIntent == null)
		{
			if (other.uaaUserIntent != null)
				return false;
		} else if (!uaaUserIntent.equals(other.uaaUserIntent))
			return false;
		return true;
	}
}

