package com.engineerbetter.converger.intents;

import java.util.Optional;

import com.engineerbetter.converger.facade.CloudFoundryFacade;
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
	}

}
