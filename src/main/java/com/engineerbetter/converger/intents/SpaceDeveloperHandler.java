package com.engineerbetter.converger.intents;

public abstract class SpaceDeveloperHandler extends Handler<SpaceDeveloperIntent>
{
	public SpaceDeveloperHandler(SpaceDeveloperIntent intent)
	{
		super(intent);
	}


	@Override
	public String getPlanAction()
	{
		String not = intent.getResolution().exists() ? "not " : "";
		return "Would "+not+"set "+intent.userIntent.uaaUserIntent.properties.email+" as developer of "+intent.spaceIntent.name.name;
	}
}
