package com.engineerbetter.converger.intents;

import com.engineerbetter.converger.facade.UaaFacade;
import com.engineerbetter.converger.resolution.Resolution;

public abstract class UaaFacadeHandlerBuilder<I extends Intent<? extends Resolution>> implements HandlerBuilder<I>
{
	protected final UaaFacade uaa;

	public UaaFacadeHandlerBuilder(UaaFacade uaa)
	{
		this.uaa = uaa;
	}
}
