package com.engineerbetter.converger.resolution;

public class Difference
{
	private final String fieldName;
	private final Object desired;
	private final Object observed;


	public Difference(String fieldName, Object desired, Object observed)
	{
		this.fieldName = fieldName;
		this.desired = desired;
		this.observed = observed;
	}


	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((desired == null) ? 0 : desired.hashCode());
		result = prime * result
				+ ((fieldName == null) ? 0 : fieldName.hashCode());
		result = prime * result
				+ ((observed == null) ? 0 : observed.hashCode());
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
		Difference other = (Difference) obj;
		if (desired == null)
		{
			if (other.desired != null)
				return false;
		} else if (!desired.equals(other.desired))
			return false;
		if (fieldName == null)
		{
			if (other.fieldName != null)
				return false;
		} else if (!fieldName.equals(other.fieldName))
			return false;
		if (observed == null)
		{
			if (other.observed != null)
				return false;
		} else if (!observed.equals(other.observed))
			return false;
		return true;
	}


	@Override
	public String toString()
	{
		return fieldName + ": desired=" + desired + ", observed=" + observed;
	}
}
