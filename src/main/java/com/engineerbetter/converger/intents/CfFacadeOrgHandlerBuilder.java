package com.engineerbetter.converger.intents;

import com.engineerbetter.converger.facade.CloudFoundryFacade;

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
