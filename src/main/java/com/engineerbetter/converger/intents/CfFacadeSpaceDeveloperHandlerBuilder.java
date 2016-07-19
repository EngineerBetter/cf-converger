package com.engineerbetter.converger.intents;

import com.engineerbetter.converger.facade.CloudFoundryFacade;

public class CfFacadeSpaceDeveloperHandlerBuilder extends CfFacadeHandlerBuilder<SpaceDeveloperIntent>
{

	public CfFacadeSpaceDeveloperHandlerBuilder(CloudFoundryFacade cf)
	{
		super(cf);
	}

	@Override
	public Handler<SpaceDeveloperIntent> build(SpaceDeveloperIntent intent)
	{
		return new CfFacadeSpaceDeveloperHandler(intent, cf);
	}
}
