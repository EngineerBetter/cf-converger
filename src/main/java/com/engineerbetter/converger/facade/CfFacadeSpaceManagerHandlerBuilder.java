package com.engineerbetter.converger.facade;

import com.engineerbetter.converger.intents.Handler;
import com.engineerbetter.converger.intents.SpaceManagerIntent;

public class CfFacadeSpaceManagerHandlerBuilder extends CfFacadeHandlerBuilder<SpaceManagerIntent>
{

	public CfFacadeSpaceManagerHandlerBuilder(CloudFoundryFacade cf)
	{
		super(cf);
	}

	@Override
	public Handler<SpaceManagerIntent> build(SpaceManagerIntent intent)
	{
		return new CfFacadeSpaceManagerHandler(intent, cf);
	}
}
