package com.engineerbetter.converger.facade;

import java.util.Optional;

import com.engineerbetter.converger.facade.CloudFoundryFacade.SpaceRole;
import com.engineerbetter.converger.intents.SpaceAuditorHandler;
import com.engineerbetter.converger.intents.SpaceAuditorIntent;
import com.engineerbetter.converger.resolution.RelationshipResolution;

public class CfFacadeSpaceAuditorHandler extends SpaceAuditorHandler
{
	private final CloudFoundryFacade cf;

	public CfFacadeSpaceAuditorHandler(SpaceAuditorIntent intent, CloudFoundryFacade cf)
	{
		super(intent);
		this.cf = cf;
	}

	@Override
	public void resolve()
	{
		Optional<String> spaceId = intent.spaceIntent.getResolution().getId();
		Optional<String> userId = intent.userIntent.getResolution().getId();

		if(spaceId.isPresent() && userId.isPresent())
		{
			intent.setResolution(RelationshipResolution.of(cf.hasSpaceRole(userId.get(), spaceId.get(), CloudFoundryFacade.SpaceRole.AUDITOR)));
		}
		else
		{
			intent.setResolution(RelationshipResolution.absent());
		}
	}

	@Override
	public void converge()
	{
		if(! intent.getResolution().exists())
		{
			cf.setSpaceRole(intent.userIntent.getResolution().getId().get(), intent.spaceIntent.getResolution().getId().get(), SpaceRole.AUDITOR);
		}
	}

}
