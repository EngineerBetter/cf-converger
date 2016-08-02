package com.engineerbetter.converger.facade;

import com.engineerbetter.converger.intents.Handler;
import com.engineerbetter.converger.intents.SpaceAuditorIntent;

public class CfFacadeSpaceAuditorHandlerBuilder extends CfFacadeHandlerBuilder<SpaceAuditorIntent>
{

	public CfFacadeSpaceAuditorHandlerBuilder(CloudFoundryFacade cf)
	{
		super(cf);
	}

	@Override
	public Handler<SpaceAuditorIntent> build(SpaceAuditorIntent intent)
	{
		return new CfFacadeSpaceAuditorHandler(intent, cf);
	}
}
