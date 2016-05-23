package com.engineerbetter.converger.intents;

import org.springframework.beans.factory.annotation.Autowired;

import com.engineerbetter.converger.facade.CloudFoundryFacade;

public class UserOrgIntent implements RelationshipIntent
{
	private boolean present;
	private final OrgIntent orgIntent;
	private final CfUserIntent userIntent;
	@Autowired
	private CloudFoundryFacade cf;

	public UserOrgIntent(OrgIntent org, CfUserIntent user)
	{
		this.orgIntent = org;
		this.userIntent = user;
	}

	@Override
	public void resolve()
	{
		if(orgIntent.id().isPresent() && userIntent.id().isPresent())
		{
			present = cf.isUserInOrg(userIntent.id().get(), orgIntent.id().get());
		}
	}

	@Override
	public boolean present()
	{
		return present;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((orgIntent == null) ? 0 : orgIntent.hashCode());
		result = prime * result + (present ? 1231 : 1237);
		result = prime * result
				+ ((userIntent == null) ? 0 : userIntent.hashCode());
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
		UserOrgIntent other = (UserOrgIntent) obj;
		if (orgIntent == null)
		{
			if (other.orgIntent != null)
				return false;
		} else if (!orgIntent.equals(other.orgIntent))
			return false;
		if (present != other.present)
			return false;
		if (userIntent == null)
		{
			if (other.userIntent != null)
				return false;
		} else if (!userIntent.equals(other.userIntent))
			return false;
		return true;
	}
}
