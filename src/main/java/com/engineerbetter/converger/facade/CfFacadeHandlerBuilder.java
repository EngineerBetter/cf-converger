package com.engineerbetter.converger.facade;

import com.engineerbetter.converger.intents.HandlerBuilder;
import com.engineerbetter.converger.intents.Intent;
import com.engineerbetter.converger.resolution.Resolution;

public abstract class CfFacadeHandlerBuilder<I extends Intent<? extends Resolution>> implements HandlerBuilder<I>
{
	protected final CloudFoundryFacade cf;

	public CfFacadeHandlerBuilder(CloudFoundryFacade cf)
	{
		this.cf = cf;
	}
}
