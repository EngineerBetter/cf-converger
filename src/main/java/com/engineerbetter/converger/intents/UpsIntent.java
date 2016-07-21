package com.engineerbetter.converger.intents;

import com.engineerbetter.converger.properties.UpsProperties;
import com.engineerbetter.converger.resolution.IdentifiableResolution;

public class UpsIntent implements Intent<IdentifiableResolution>
{
	public final UpsProperties upsProperties;
	public final SpaceIntent spaceIntent;
	private IdentifiableResolution resolution;

	public UpsIntent(UpsProperties upsProperties, SpaceIntent spaceIntent)
	{
		this.upsProperties = upsProperties;
		this.spaceIntent = spaceIntent;
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
				+ ((spaceIntent == null) ? 0 : spaceIntent.hashCode());
		result = prime * result
				+ ((upsProperties == null) ? 0 : upsProperties.hashCode());
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
		if (spaceIntent == null)
		{
			if (other.spaceIntent != null)
				return false;
		} else if (!spaceIntent.equals(other.spaceIntent))
			return false;
		if (upsProperties == null)
		{
			if (other.upsProperties != null)
				return false;
		} else if (!upsProperties.equals(other.upsProperties))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "UpsIntent [upsProperties=" + upsProperties + ", spaceIntent="
				+ spaceIntent + "]";
	}
}
