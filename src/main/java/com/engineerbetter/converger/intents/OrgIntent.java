package com.engineerbetter.converger.intents;

import com.engineerbetter.converger.properties.NameProperty;
import com.engineerbetter.converger.resolution.IdentifiableResolution;

public final class OrgIntent implements Intent<IdentifiableResolution>
{
	public final NameProperty name;
	private IdentifiableResolution resolution;


	public OrgIntent(NameProperty name)
	{
		this.name = name;
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
		OrgIntent other = (OrgIntent) obj;
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
		return "OrgIntent [name=" + name + "]";
	}
}
