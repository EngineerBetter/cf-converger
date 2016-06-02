package com.engineerbetter.converger.intents;

import java.util.Optional;

import com.engineerbetter.converger.facade.CloudFoundryFacade;
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
	}

}
