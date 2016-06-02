package com.engineerbetter.converger.intents;

public class CfFacadeSpaceHandlerBuilder implements HandlerBuilder<SpaceIntent>
{
	@Override
	public Handler<SpaceIntent> build(SpaceIntent intent)
	{
		return new CfFacadeSpaceHandler(intent, null);
	}
}
