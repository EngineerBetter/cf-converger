package com.engineerbetter.converger.intents;

import com.engineerbetter.converger.facade.CloudFoundryFacade;

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
