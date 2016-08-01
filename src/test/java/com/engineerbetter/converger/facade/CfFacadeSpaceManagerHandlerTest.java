package com.engineerbetter.converger.facade;

import com.engineerbetter.converger.facade.CloudFoundryFacade.SpaceRole;
import com.engineerbetter.converger.intents.SpaceManagerIntent;

public class CfFacadeSpaceManagerHandlerTest extends CfFacadeSpaceRoleHandlerTest<SpaceManagerIntent, CfFacadeSpaceManagerHandler>
{
	@Override
	protected void setupInstances()
	{
		intent = new SpaceManagerIntent(spaceIntent, cfUserIntent);
		handler = new CfFacadeSpaceManagerHandler(intent, cf);
	}


	@Override
	SpaceRole getRole()
	{
		return SpaceRole.MANAGER;
	}
}
