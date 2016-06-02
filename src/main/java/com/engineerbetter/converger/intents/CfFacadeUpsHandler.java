package com.engineerbetter.converger.intents;

import java.util.Optional;

import com.engineerbetter.converger.facade.CloudFoundryFacade;

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
			cf.findUps(intent.upsProperties.name, spaceId.get());
		}
	}

	@Override
	public void converge()
	{
	}

}
