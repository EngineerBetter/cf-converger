package com.engineerbetter.converger.intents;


public abstract class OrgAuditorHandler extends Handler<OrgAuditorIntent>
{
	protected OrgAuditorHandler(OrgAuditorIntent intent)
	{
		super(intent);
	}


	@Override
	public String getPlanAction()
	{
		String not = intent.getResolution().exists() ? "not " : "";
		return "Would "+not+"set "+intent.userIntent.uaaUserIntent.properties.email+" as auditor of "+intent.orgIntent.name.name;
	}
}
