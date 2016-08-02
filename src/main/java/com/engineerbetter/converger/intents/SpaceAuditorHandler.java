package com.engineerbetter.converger.intents;

public abstract class SpaceAuditorHandler extends Handler<SpaceAuditorIntent>
{
	public SpaceAuditorHandler(SpaceAuditorIntent intent)
	{
		super(intent);
	}


	@Override
	public String getPlanAction()
	{
		String not = intent.getResolution().exists() ? "not " : "";
		return "Would "+not+"set "+intent.userIntent.uaaUserIntent.properties.email+" as auditor of "+intent.spaceIntent.name.name;
	}
}
