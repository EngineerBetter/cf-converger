package com.engineerbetter.converger.intents;

public class CfFacadeSpaceDeveloperHandlerBuilder implements HandlerBuilder<SpaceDeveloperIntent>
{
	@Override
	public Handler<SpaceDeveloperIntent> build(SpaceDeveloperIntent intent)
	{
		return new CfFacadeSpaceDeveloperHandler(intent, null);
	}
}
