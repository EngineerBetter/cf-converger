package com.engineerbetter.converger.resolution;

public class RelationshipResolution implements Resolution
{
	public static RelationshipResolution of(boolean exists)
	{
		return new RelationshipResolution(exists);
	}

	public static RelationshipResolution absent()
	{
		return new RelationshipResolution(false);
	}

	private final boolean exists;

	private RelationshipResolution(boolean exists)
	{
		this.exists = exists;
	}

	@Override
	public boolean exists()
	{
		return exists;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (exists ? 1231 : 1237);
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
		RelationshipResolution other = (RelationshipResolution) obj;
		if (exists != other.exists)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "RelationshipResolution [exists=" + exists + "]";
	}
}
