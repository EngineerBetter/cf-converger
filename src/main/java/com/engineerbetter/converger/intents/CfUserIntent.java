package com.engineerbetter.converger.intents;

import com.engineerbetter.converger.resolution.IdentifiableResolution;

public class CfUserIntent implements Intent<IdentifiableResolution>
{
	public final UaaUserIntent uaaUserIntent;
	private IdentifiableResolution resolution;

	public CfUserIntent(UaaUserIntent uaaUserIntent)
	{
		this.uaaUserIntent = uaaUserIntent;
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
		if (uaaUserIntent == null)
		{
			if (other.uaaUserIntent != null)
				return false;
		} else if (!uaaUserIntent.equals(other.uaaUserIntent))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "CfUserIntent [uaaUserIntent=" + uaaUserIntent + "]";
	}
}
