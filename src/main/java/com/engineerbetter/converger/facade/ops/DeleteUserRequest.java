package com.engineerbetter.converger.facade.ops;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteUserRequest
{
	public final String guid;
	public final boolean async;


	@JsonCreator
	public DeleteUserRequest(@JsonProperty("guid") String guid, @JsonProperty("async") boolean async)
	{
		this.guid = guid;
		this.async = async;
	}


	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (async ? 1231 : 1237);
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
		DeleteUserRequest other = (DeleteUserRequest) obj;
		if (async != other.async)
			return false;
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
		return "DeleteUserRequest [guid=" + guid + ", async=" + async + "]";
	}
}