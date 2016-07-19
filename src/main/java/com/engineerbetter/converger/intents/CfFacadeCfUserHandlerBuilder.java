package com.engineerbetter.converger.intents;

import com.engineerbetter.converger.facade.CloudFoundryFacade;

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
