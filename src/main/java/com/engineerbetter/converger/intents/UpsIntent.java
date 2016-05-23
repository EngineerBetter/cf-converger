package com.engineerbetter.converger.intents;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.engineerbetter.converger.facade.CloudFoundryFacade;

public class UpsIntent implements IdentifiableIntent
{
	public final String name;
	public final Map<String, String> credentials;
	private final SpaceIntent spaceIntent;
	private Optional<String> id = Optional.empty();
	@Autowired
	private CloudFoundryFacade cf;

	public UpsIntent(String name, Map<String, String> credentials, SpaceIntent space)
	{
		this.name = name;
		this.credentials = Collections.unmodifiableMap(credentials);
		this.spaceIntent = space;
	}

	@Override
	public Optional<String> id()
	{
		return id;
	}

	@Override
	public void resolve()
	{
		if(spaceIntent.id().isPresent())
		{
			cf.findUps(name, spaceIntent.id().get());
		}
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((credentials == null) ? 0 : credentials.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((spaceIntent == null) ? 0 : spaceIntent.hashCode());
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
		UpsIntent other = (UpsIntent) obj;
		if (credentials == null)
		{
			if (other.credentials != null)
				return false;
		} else if (!credentials.equals(other.credentials))
			return false;
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
		if (spaceIntent == null)
		{
			if (other.spaceIntent != null)
				return false;
		} else if (!spaceIntent.equals(other.spaceIntent))
			return false;
		return true;
	}
}
