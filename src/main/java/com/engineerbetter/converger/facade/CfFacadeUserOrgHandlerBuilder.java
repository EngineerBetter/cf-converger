package com.engineerbetter.converger.facade;

import com.engineerbetter.converger.intents.Handler;
import com.engineerbetter.converger.intents.UserOrgIntent;

public class CfFacadeUserOrgHandlerBuilder extends CfFacadeHandlerBuilder<UserOrgIntent>
{

	public CfFacadeUserOrgHandlerBuilder(CloudFoundryFacade cf)
	{
		super(cf);
	}

	@Override
	public Handler<UserOrgIntent> build(UserOrgIntent intent)
	{
		return new CfFacadeUserOrgHandler(intent, cf);
	}
}
