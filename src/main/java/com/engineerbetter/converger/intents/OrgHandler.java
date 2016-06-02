package com.engineerbetter.converger.intents;


public abstract class OrgHandler extends Handler<OrgIntent>
{
	protected OrgHandler(OrgIntent intent)
	{
		super(intent);
	}
}
