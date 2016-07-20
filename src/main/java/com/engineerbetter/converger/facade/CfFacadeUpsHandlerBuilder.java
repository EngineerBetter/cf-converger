package com.engineerbetter.converger.facade;

import com.engineerbetter.converger.intents.Handler;
import com.engineerbetter.converger.intents.UpsIntent;

public class CfFacadeUpsHandlerBuilder extends CfFacadeHandlerBuilder<UpsIntent>
{

	public CfFacadeUpsHandlerBuilder(CloudFoundryFacade cf)
	{
		super(cf);
	}

	@Override
	public Handler<UpsIntent> build(UpsIntent intent)
	{
		return new CfFacadeUpsHandler(intent, cf);
	}
}
