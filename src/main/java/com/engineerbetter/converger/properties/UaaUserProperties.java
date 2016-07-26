package com.engineerbetter.converger.properties;

public class UaaUserProperties
{
	public final String email;
	public final String givenName;
	public final String familyName;

	public UaaUserProperties(String email, String givenName, String familyName)
	{
		this.email = email;
		this.givenName = givenName;
		this.familyName = familyName;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((familyName == null) ? 0 : familyName.hashCode());
		result = prime * result
				+ ((givenName == null) ? 0 : givenName.hashCode());
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
		UaaUserProperties other = (UaaUserProperties) obj;
		if (email == null)
		{
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (familyName == null)
		{
			if (other.familyName != null)
				return false;
		} else if (!familyName.equals(other.familyName))
			return false;
		if (givenName == null)
		{
			if (other.givenName != null)
				return false;
		} else if (!givenName.equals(other.givenName))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "UaaUserProperties [email=" + email + ", givenName=" + givenName
				+ ", familyName=" + familyName + "]";
	}
}
