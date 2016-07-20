package com.engineerbetter.converger.facade;

import com.engineerbetter.converger.intents.HandlerBuilder;
import com.engineerbetter.converger.intents.Intent;
import com.engineerbetter.converger.resolution.Resolution;

public abstract class UaaFacadeHandlerBuilder<I extends Intent<? extends Resolution>> implements HandlerBuilder<I>
{
	protected final UaaFacade uaa;

	public UaaFacadeHandlerBuilder(UaaFacade uaa)
	{
		this.uaa = uaa;
	}
}
