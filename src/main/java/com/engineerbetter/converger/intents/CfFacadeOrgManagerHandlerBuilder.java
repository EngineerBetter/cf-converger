package com.engineerbetter.converger.intents;

import com.engineerbetter.converger.facade.CloudFoundryFacade;

public class CfFacadeOrgManagerHandlerBuilder extends CfFacadeHandlerBuilder<OrgManagerIntent>
{

	public CfFacadeOrgManagerHandlerBuilder(CloudFoundryFacade cf)
	{
		super(cf);
	}

	@Override
	public Handler<OrgManagerIntent> build(OrgManagerIntent intent)
	{
		return new CfFacadeOrgManagerHandler(intent, cf);
	}
}
