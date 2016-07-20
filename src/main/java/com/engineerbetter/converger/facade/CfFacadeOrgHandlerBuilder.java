package com.engineerbetter.converger.facade;

import com.engineerbetter.converger.intents.Handler;
import com.engineerbetter.converger.intents.OrgIntent;

public class CfFacadeOrgHandlerBuilder extends CfFacadeHandlerBuilder<OrgIntent>
{

	public CfFacadeOrgHandlerBuilder(CloudFoundryFacade cf)
	{
		super(cf);
	}

	@Override
	public Handler<OrgIntent> build(OrgIntent intent)
	{
		return new CfFacadeOrgHandler(intent, cf);
	}
}
