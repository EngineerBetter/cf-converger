package com.engineerbetter.converger.facade;

import com.engineerbetter.converger.intents.CfUserIntent;
import com.engineerbetter.converger.intents.Handler;

public class CfFacadeCfUserHandlerBuilder extends CfFacadeHandlerBuilder<CfUserIntent>
{
	public CfFacadeCfUserHandlerBuilder(CloudFoundryFacade cf)
	{
		super(cf);
	}

	@Override
	public Handler<CfUserIntent> build(CfUserIntent intent)
	{
		return new CfFacadeCfUserHandler(intent, cf);
	}
}
