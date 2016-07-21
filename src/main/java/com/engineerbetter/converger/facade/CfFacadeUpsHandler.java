package com.engineerbetter.converger.facade;

import java.util.Optional;

import com.engineerbetter.converger.intents.UpsHandler;
import com.engineerbetter.converger.intents.UpsIntent;
import com.engineerbetter.converger.resolution.IdentifiableResolution;

public class CfFacadeUpsHandler extends UpsHandler
{
	private final CloudFoundryFacade cf;


	public CfFacadeUpsHandler(UpsIntent intent, CloudFoundryFacade cf)
	{
		super(intent);
		this.cf = cf;
	}

	@Override
	public void resolve()
	{
		Optional<String> spaceId = intent.spaceIntent.getResolution().getId();

		if(spaceId.isPresent())
		{
			Optional<String> upsId = cf.findUps(intent.upsProperties.name, spaceId.get());
			intent.setResolution(IdentifiableResolution.of(upsId));
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
			String spaceId = intent.spaceIntent.getResolution().getId().get();
			String upsId = cf.createUps(intent.upsProperties, spaceId);
			intent.setResolution(IdentifiableResolution.of(upsId));
		}
	}

}
