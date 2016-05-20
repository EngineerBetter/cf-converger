package com.engineerbetter.conveger.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Org
{
	public final String name;
	public final List<Space> spaces;

	@JsonCreator
	public Org(@JsonProperty("name") String name, @JsonProperty("spaces") List<Space> spaces)
	{
		this.name = name;
		this.spaces = spaces;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((spaces == null) ? 0 : spaces.hashCode());
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
		Org other = (Org) obj;
		if (name == null)
		{
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (spaces == null)
		{
			if (other.spaces != null)
				return false;
		} else if (!spaces.equals(other.spaces))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Org [name=" + name + ", spaces=" + spaces + "]";
	}


}