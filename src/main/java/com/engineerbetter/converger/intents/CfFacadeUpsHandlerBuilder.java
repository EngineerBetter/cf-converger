package com.engineerbetter.converger.intents;

public class CfFacadeUpsHandlerBuilder implements HandlerBuilder<UpsIntent>
{
	@Override
	public Handler<UpsIntent> build(UpsIntent intent)
	{
		return new CfFacadeUpsHandler(intent, null);
	}
}
