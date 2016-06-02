package com.engineerbetter.converger.intents;

public class CfFacadeOrgHandlerBuilder implements HandlerBuilder<OrgIntent>
{
	@Override
	public Handler<OrgIntent> build(OrgIntent intent)
	{
		return new CfFacadeOrgHandler(intent, null);
	}
}
