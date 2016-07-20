package com.engineerbetter.converger.facade;

import com.engineerbetter.converger.intents.Handler;
import com.engineerbetter.converger.intents.SpaceIntent;

public class CfFacadeSpaceHandlerBuilder extends CfFacadeHandlerBuilder<SpaceIntent>
{

	public CfFacadeSpaceHandlerBuilder(CloudFoundryFacade cf)
	{
		super(cf);
	}

	@Override
	public Handler<SpaceIntent> build(SpaceIntent intent)
	{
		return new CfFacadeSpaceHandler(intent, cf);
	}
}
