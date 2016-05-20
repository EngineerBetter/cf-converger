package com.engineerbetter.conveger.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Declaration
{
	public final Org org;

	@JsonCreator
	public Declaration(@JsonProperty("org") Org org) {
		this.org = org;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((org == null) ? 0 : org.hashCode());
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
		Declaration other = (Declaration) obj;
		if (org == null)
		{
			if (other.org != null)
				return false;
		} else if (!org.equals(other.org))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Declaration [org=" + org + "]";
	}

}
