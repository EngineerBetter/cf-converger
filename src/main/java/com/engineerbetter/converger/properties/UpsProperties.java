package com.engineerbetter.converger.properties;

import java.util.Map;

public class UpsProperties
{
	public final String name;
	public final Map<String, String> credentials;

	public UpsProperties(String name, Map<String, String> credentials)
	{
		this.name = name;
		this.credentials = credentials;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((credentials == null) ? 0 : credentials.hashCode());
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
		UpsProperties other = (UpsProperties) obj;
		if (credentials == null)
		{
			if (other.credentials != null)
				return false;
		} else if (!credentials.equals(other.credentials))
			return false;
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
		return "UpsProperties [name=" + name + ", credentials=" + credentials
				+ "]";
	}
}
