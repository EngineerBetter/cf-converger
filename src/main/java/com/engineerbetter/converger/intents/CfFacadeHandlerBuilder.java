package com.engineerbetter.converger.intents;

import com.engineerbetter.converger.facade.CloudFoundryFacade;
import com.engineerbetter.converger.resolution.Resolution;

public abstract class CfFacadeHandlerBuilder<I extends Intent<? extends Resolution>> implements HandlerBuilder<I>
{
	protected final CloudFoundryFacade cf;

	public CfFacadeHandlerBuilder(CloudFoundryFacade cf)
	{
		this.cf = cf;
	}
}
