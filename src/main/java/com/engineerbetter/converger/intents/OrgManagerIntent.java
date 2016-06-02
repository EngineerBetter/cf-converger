package com.engineerbetter.converger.intents;

import com.engineerbetter.converger.resolution.RelationshipResolution;


public class OrgManagerIntent implements Intent<RelationshipResolution>
{
	public final OrgIntent orgIntent;
	public final CfUserIntent userIntent;
	private RelationshipResolution resolution;

	public OrgManagerIntent(OrgIntent org, CfUserIntent user)
	{
		this.orgIntent = org;
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
				+ ((orgIntent == null) ? 0 : orgIntent.hashCode());
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
		OrgManagerIntent other = (OrgManagerIntent) obj;
		if (orgIntent == null)
		{
			if (other.orgIntent != null)
				return false;
		} else if (!orgIntent.equals(other.orgIntent))
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
		return "OrgManagerIntent [orgIntent=" + orgIntent + ", userIntent="
				+ userIntent + "]";
	}
}
