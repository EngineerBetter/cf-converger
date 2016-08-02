package com.engineerbetter.converger.facade;

import com.engineerbetter.converger.facade.CloudFoundryFacade.SpaceRole;
import com.engineerbetter.converger.intents.SpaceAuditorIntent;

public class CfFacadeSpaceAuditorHandlerTest extends CfFacadeSpaceRoleHandlerTest<SpaceAuditorIntent, CfFacadeSpaceAuditorHandler>
{
	@Override
	protected void setupInstances()
	{
		intent = new SpaceAuditorIntent(spaceIntent, cfUserIntent);
		handler = new CfFacadeSpaceAuditorHandler(intent, cf);
	}


	@Override
	SpaceRole getRole()
	{
		return SpaceRole.AUDITOR;
	}
}
