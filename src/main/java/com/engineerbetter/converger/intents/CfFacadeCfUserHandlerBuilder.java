package com.engineerbetter.converger.intents;

public class CfFacadeCfUserHandlerBuilder implements HandlerBuilder<CfUserIntent>
{
	@Override
	public Handler<CfUserIntent> build(CfUserIntent intent)
	{
		return new CfFacadeCfUserHandler(intent, null);
	}
}
