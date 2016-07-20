package com.engineerbetter.converger.intents;

import java.util.Optional;

import com.engineerbetter.converger.facade.CloudFoundryFacade;
import com.engineerbetter.converger.resolution.RelationshipResolution;

public class CfFacadeOrgManagerHandler extends OrgManagerHandler
{
	private final CloudFoundryFacade cf;

	public CfFacadeOrgManagerHandler(OrgManagerIntent intent, CloudFoundryFacade cf)
	{
		super(intent);
		this.cf = cf;
	}

	@Override
	public void resolve()
	{
		Optional<String> orgId = intent.orgIntent.getResolution().getId();
		Optional<String> userId = intent.userIntent.getResolution().getId();

		if(orgId.isPresent() && userId.isPresent())
		{
			intent.setResolution(RelationshipResolution.of(cf.hasOrgRole(userId.get(), orgId.get(), CloudFoundryFacade.OrgRole.ORG_MANAGER)));
		}
		else
		{
			intent.setResolution(RelationshipResolution.absent());
		}
	}


	@Override
	public void converge()
	{
	}
}