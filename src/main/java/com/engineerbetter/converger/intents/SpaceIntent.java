package com.engineerbetter.converger.intents;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.engineerbetter.converger.facade.CloudFoundryFacade;

public class SpaceIntent implements IdentifiableIntent
{
	public final String name;
	public final OrgIntent orgIntent;
	private Optional<String> id;
	@Autowired
	private CloudFoundryFacade cf;

	public SpaceIntent(String name, OrgIntent org)
	{
		this.name = name;
		this.orgIntent = org;
	}

	@Override
	public void resolve()
	{
		if(orgIntent.id().isPresent())
		{
			this.id = cf.findSpace(name, orgIntent.id().get());
		}
	}

	@Override
	public Optional<String> id()
	{
		return this.id;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((orgIntent == null) ? 0 : orgIntent.hashCode());
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
		if (orgIntent == null)
		{
			if (other.orgIntent != null)
				return false;
		} else if (!orgIntent.equals(other.orgIntent))
			return false;
		return true;
	}
}
