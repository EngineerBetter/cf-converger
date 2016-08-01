package com.engineerbetter.converger.intents;

import com.engineerbetter.converger.resolution.RelationshipResolution;


public class SpaceManagerIntent implements Intent<RelationshipResolution>
{
	public final SpaceIntent spaceIntent;
	public final CfUserIntent userIntent;
	private RelationshipResolution resolution;

	public SpaceManagerIntent(SpaceIntent space, CfUserIntent user)
	{
		this.spaceIntent = space;
		this.userIntent = user;
	}


	@Override
	public RelationshipResolution getResolution()
	{
		return resolution;
	}

	@Override
	public void setResolution(RelationshipResolution resolution)
	{
		this.resolution = resolution;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
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
		SpaceManagerIntent other = (SpaceManagerIntent) obj;
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

	@Override
	public String toString()
	{
		return "SpaceManagerIntent [spaceIntent=" + spaceIntent
				+ ", userIntent=" + userIntent + "]";
	}
}
