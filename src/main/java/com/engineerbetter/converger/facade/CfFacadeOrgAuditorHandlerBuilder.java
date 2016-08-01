package com.engineerbetter.converger.facade;

import com.engineerbetter.converger.intents.Handler;
import com.engineerbetter.converger.intents.OrgAuditorIntent;

public class CfFacadeOrgAuditorHandlerBuilder extends CfFacadeHandlerBuilder<OrgAuditorIntent>
{

	public CfFacadeOrgAuditorHandlerBuilder(CloudFoundryFacade cf)
	{
		super(cf);
	}

	@Override
	public Handler<OrgAuditorIntent> build(OrgAuditorIntent intent)
	{
		return new CfFacadeOrgAuditorHandler(intent, cf);
	}
}
