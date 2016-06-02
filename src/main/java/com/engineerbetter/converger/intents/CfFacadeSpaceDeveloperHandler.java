package com.engineerbetter.converger.intents;

import java.util.Optional;

import com.engineerbetter.converger.facade.CloudFoundryFacade;
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
			intent.setResolution(RelationshipResolution.of(cf.hasSpaceRole(userId.get(), spaceId.get(), CloudFoundryFacade.SpaceRole.SPACE_DEVELOPER)));
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
