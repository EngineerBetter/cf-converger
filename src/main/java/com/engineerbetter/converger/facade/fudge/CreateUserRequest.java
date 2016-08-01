package com.engineerbetter.converger.facade.fudge;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateUserRequest
{
	public final String guid;

	@JsonCreator
	public CreateUserRequest(@JsonProperty("guid") String guid)
	{
		this.guid = guid;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((guid == null) ? 0 : guid.hashCode());
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
		CreateUserRequest other = (CreateUserRequest) obj;
		if (guid == null)
		{
			if (other.guid != null)
				return false;
		} else if (!guid.equals(other.guid))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "CreateUserRequest [guid=" + guid + "]";
	}
}