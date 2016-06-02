package com.engineerbetter.converger.intents;

public class CfFacadeOrgManagerHandlerBuilder implements HandlerBuilder<OrgManagerIntent>
{
	@Override
	public Handler<OrgManagerIntent> build(OrgManagerIntent intent)
	{
		return new CfFacadeOrgManagerHandler(intent, null);
	}
}
