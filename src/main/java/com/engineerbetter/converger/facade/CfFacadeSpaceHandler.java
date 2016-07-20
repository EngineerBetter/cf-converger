package com.engineerbetter.converger.facade;

import java.util.Optional;

import com.engineerbetter.converger.intents.SpaceHandler;
import com.engineerbetter.converger.intents.SpaceIntent;
import com.engineerbetter.converger.resolution.IdentifiableResolution;

public class CfFacadeSpaceHandler extends SpaceHandler
{
	private final CloudFoundryFacade cf;


	public CfFacadeSpaceHandler(SpaceIntent intent, CloudFoundryFacade cf)
	{
		super(intent);
		this.cf = cf;
	}

	@Override
	public void resolve()
	{
		Optional<String> orgId = intent.orgIntent.getResolution().getId();

		if(orgId.isPresent())
		{
			intent.setResolution(IdentifiableResolution.of(cf.findSpace(intent.name.name, orgId.get())));
		}
		else
		{
			intent.setResolution(IdentifiableResolution.absent());
		}
	}

	@Override
	public void converge()
	{
		if(! intent.getResolution().exists())
		{
			String spaceId = cf.createSpace(intent.name.name, intent.orgIntent.getResolution().getId().get());
			intent.setResolution(IdentifiableResolution.of(spaceId));
		}
	}

}
