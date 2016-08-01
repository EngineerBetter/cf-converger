package com.engineerbetter.converger.facade;

import java.util.Optional;

import com.engineerbetter.converger.intents.UserOrgHandler;
import com.engineerbetter.converger.intents.UserOrgIntent;
import com.engineerbetter.converger.resolution.RelationshipResolution;

public class CfFacadeUserOrgHandler extends UserOrgHandler
{
	private final CloudFoundryFacade cf;


	public CfFacadeUserOrgHandler(UserOrgIntent intent, CloudFoundryFacade cf)
	{
		super(intent);
		this.cf = cf;
	}

	@Override
	public void resolve()
	{
		Optional<String> orgId = intent.orgIntent.getResolution().getId();
		Optional<String> userId = intent.userIntent.getResolution().getId();

		if(orgId.isPresent() && intent.userIntent.getResolution().getId().isPresent())
		{
			intent.setResolution(RelationshipResolution.of(cf.isUserInOrg(userId.get(), orgId.get())));
		}
		else
		{
			intent.setResolution(RelationshipResolution.of(false));
		}
	}

	@Override
	public void converge()
	{
		if(! intent.getResolution().exists())
		{
			cf.addUserToOrg(intent.userIntent.getResolution().getId().get(), intent.orgIntent.getResolution().getId().get());
		}
	}

}
