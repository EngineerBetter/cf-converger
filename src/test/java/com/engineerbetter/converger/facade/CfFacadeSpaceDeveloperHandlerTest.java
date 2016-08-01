package com.engineerbetter.converger.facade;

import com.engineerbetter.converger.facade.CloudFoundryFacade.SpaceRole;
import com.engineerbetter.converger.intents.SpaceDeveloperIntent;

public class CfFacadeSpaceDeveloperHandlerTest extends CfFacadeSpaceRoleHandlerTest<SpaceDeveloperIntent, CfFacadeSpaceDeveloperHandler>
{
	@Override
	protected void setupInstances()
	{
		intent = new SpaceDeveloperIntent(spaceIntent, cfUserIntent);
		handler = new CfFacadeSpaceDeveloperHandler(intent, cf);
	}


	@Override
	SpaceRole getRole()
	{
		return SpaceRole.DEVELOPER;
	}
}
