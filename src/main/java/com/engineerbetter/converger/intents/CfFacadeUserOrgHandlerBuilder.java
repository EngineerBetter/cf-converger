package com.engineerbetter.converger.intents;

public class CfFacadeUserOrgHandlerBuilder implements HandlerBuilder<UserOrgIntent>
{
	@Override
	public Handler<UserOrgIntent> build(UserOrgIntent intent)
	{
		return new CfFacadeUserOrgHandler(intent, null);
	}
}
