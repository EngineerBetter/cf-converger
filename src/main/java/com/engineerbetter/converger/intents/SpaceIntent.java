package com.engineerbetter.converger.intents;

import com.engineerbetter.converger.properties.NameProperty;
import com.engineerbetter.converger.resolution.IdentifiableResolution;

public class SpaceIntent implements Intent<IdentifiableResolution>
{
	public final NameProperty name;
	public final OrgIntent orgIntent;
	private IdentifiableResolution resolution;

	public SpaceIntent(NameProperty name, OrgIntent orgIntent)
	{
		this.name = name;
		this.orgIntent = orgIntent;
	}

	@Override
	public IdentifiableResolution getResolution()
	{
		return resolution;
	}

	@Override
	public void setResolution(IdentifiableResolution resolution)
	{
		this.resolution = resolution;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
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

	@Override
	public String toString()
	{
		return "SpaceIntent [name=" + name + ", orgIntent=" + orgIntent + "]";
	}
}
