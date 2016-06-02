package com.engineerbetter.converger.intents;


public abstract class OrgManagerHandler extends Handler<OrgManagerIntent>
{
	protected OrgManagerHandler(OrgManagerIntent intent)
	{
		super(intent);
	}
}
