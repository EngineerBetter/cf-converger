package com.engineerbetter.converger.facade;

import com.engineerbetter.converger.intents.Handler;
import com.engineerbetter.converger.intents.SpaceDeveloperIntent;

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
