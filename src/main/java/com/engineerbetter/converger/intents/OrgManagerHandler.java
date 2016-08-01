package com.engineerbetter.converger.intents;


public abstract class OrgManagerHandler extends Handler<OrgManagerIntent>
{
	protected OrgManagerHandler(OrgManagerIntent intent)
	{
		super(intent);
	}


	@Override
	public String getPlanAction()
	{
		String not = intent.getResolution().exists() ? "not " : "";
		return "Would "+not+"set "+intent.userIntent.uaaUserIntent.properties.email+" as manager of "+intent.orgIntent.name.name;
	}
}
