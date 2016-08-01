package com.engineerbetter.converger.intents;

public abstract class SpaceManagerHandler extends Handler<SpaceManagerIntent>
{
	public SpaceManagerHandler(SpaceManagerIntent intent)
	{
		super(intent);
	}


	@Override
	public String getPlanAction()
	{
		String not = intent.getResolution().exists() ? "not " : "";
		return "Would "+not+"set "+intent.userIntent.uaaUserIntent.properties.email+" as manager of "+intent.spaceIntent.name.name;
	}
}
