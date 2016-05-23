package com.engineerbetter.converger.intents;

import org.springframework.beans.factory.annotation.Autowired;

import com.engineerbetter.converger.facade.CloudFoundryFacade;

public class SpaceDeveloperIntent implements RelationshipIntent
{
	private final SpaceIntent spaceIntent;
	private final CfUserIntent userIntent;
	private boolean present;
	@Autowired
	private CloudFoundryFacade cf;

	public SpaceDeveloperIntent(SpaceIntent space, CfUserIntent user)
	{
		this.spaceIntent = space;
		this.userIntent = user;
	}

	@Override
	public void resolve()
	{
		if(userIntent.id().isPresent() && spaceIntent.id().isPresent())
		{
			present = cf.hasSpaceRole(userIntent.id().get(), spaceIntent.id().get(), CloudFoundryFacade.SpaceRole.SPACE_DEVELOPER);
		}
	}

	@Override
	public boolean present()
	{
		return present;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (present ? 1231 : 1237);
		result = prime * result
				+ ((spaceIntent == null) ? 0 : spaceIntent.hashCode());
		result = prime * result
				+ ((userIntent == null) ? 0 : userIntent.hashCode());
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
		SpaceDeveloperIntent other = (SpaceDeveloperIntent) obj;
		if (present != other.present)
			return false;
		if (spaceIntent == null)
		{
			if (other.spaceIntent != null)
				return false;
		} else if (!spaceIntent.equals(other.spaceIntent))
			return false;
		if (userIntent == null)
		{
			if (other.userIntent != null)
				return false;
		} else if (!userIntent.equals(other.userIntent))
			return false;
		return true;
	}
}
