package com.engineerbetter.converger.intents;

import com.engineerbetter.converger.facade.CloudFoundryFacade;

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
