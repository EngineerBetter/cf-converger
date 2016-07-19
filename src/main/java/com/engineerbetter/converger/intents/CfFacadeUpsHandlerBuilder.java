package com.engineerbetter.converger.intents;

import com.engineerbetter.converger.facade.CloudFoundryFacade;

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
