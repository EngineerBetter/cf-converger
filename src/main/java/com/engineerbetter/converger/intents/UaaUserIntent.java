package com.engineerbetter.converger.intents;

import com.engineerbetter.converger.properties.UaaUserProperties;
import com.engineerbetter.converger.resolution.IdentifiableResolution;

public class UaaUserIntent implements Intent<IdentifiableResolution>
{
	public final UaaUserProperties properties;
	private IdentifiableResolution resolution;

	public UaaUserIntent(UaaUserProperties properties)
	{
		this.properties = properties;
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
		result = prime * result
				+ ((properties == null) ? 0 : properties.hashCode());
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
		if (properties == null)
		{
			if (other.properties != null)
				return false;
		} else if (!properties.equals(other.properties))
			return false;
		return true;
	}


	@Override
	public String toString()
	{
		return "UaaUserIntent [properties=" + properties + "]";
	}
}
