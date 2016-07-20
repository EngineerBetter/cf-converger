package com.engineerbetter.converger.facade;

import com.engineerbetter.converger.intents.Handler;
import com.engineerbetter.converger.intents.UaaUserIntent;

public class UaaFacadeUaaUserHandlerBuilder extends UaaFacadeHandlerBuilder<UaaUserIntent>
{

	public UaaFacadeUaaUserHandlerBuilder(UaaFacade uaa)
	{
		super(uaa);
	}

	@Override
	public Handler<UaaUserIntent> build(UaaUserIntent intent)
	{
		return new UaaFacadeUaaUserHandler(intent, uaa);
	}
}
