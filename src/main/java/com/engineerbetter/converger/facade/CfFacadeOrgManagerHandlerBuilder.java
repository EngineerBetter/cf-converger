package com.engineerbetter.converger.facade;

import com.engineerbetter.converger.intents.Handler;
import com.engineerbetter.converger.intents.OrgManagerIntent;

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
