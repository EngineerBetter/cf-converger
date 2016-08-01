package com.engineerbetter.converger.facade;

import java.util.Optional;

import com.engineerbetter.converger.facade.CloudFoundryFacade.SpaceRole;
import com.engineerbetter.converger.intents.SpaceDeveloperHandler;
import com.engineerbetter.converger.intents.SpaceDeveloperIntent;
import com.engineerbetter.converger.resolution.RelationshipResolution;

public class CfFacadeSpaceDeveloperHandler extends SpaceDeveloperHandler
{
	private final CloudFoundryFacade cf;

	public CfFacadeSpaceDeveloperHandler(SpaceDeveloperIntent intent, CloudFoundryFacade cf)
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
			intent.setResolution(RelationshipResolution.of(cf.hasSpaceRole(userId.get(), spaceId.get(), CloudFoundryFacade.SpaceRole.DEVELOPER)));
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
			cf.setSpaceRole(intent.userIntent.getResolution().getId().get(), intent.spaceIntent.getResolution().getId().get(), SpaceRole.DEVELOPER);
		}
	}

}
