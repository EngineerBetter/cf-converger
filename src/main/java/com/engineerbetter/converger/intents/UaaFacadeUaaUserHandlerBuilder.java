package com.engineerbetter.converger.intents;

import com.engineerbetter.converger.facade.UaaFacade;

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
