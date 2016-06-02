package com.engineerbetter.converger.intents;

public class UaaFacadeUaaUserHandlerBuilder implements HandlerBuilder<UaaUserIntent>
{
	@Override
	public Handler<UaaUserIntent> build(UaaUserIntent intent)
	{
		return new UaaFacadeUaaUserHandler(intent, null);
	}
}
