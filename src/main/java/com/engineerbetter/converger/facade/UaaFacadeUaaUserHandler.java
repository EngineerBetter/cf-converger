package com.engineerbetter.converger.facade;

import com.engineerbetter.converger.intents.UaaUserHandler;
import com.engineerbetter.converger.intents.UaaUserIntent;
import com.engineerbetter.converger.resolution.IdentifiableResolution;

public class UaaFacadeUaaUserHandler extends UaaUserHandler
{
	private final UaaFacade uaa;

	public UaaFacadeUaaUserHandler(UaaUserIntent intent, UaaFacade uaa)
	{
		super(intent);
		this.uaa = uaa;
	}


	@Override
	public void resolve()
	{
		intent.setResolution(IdentifiableResolution.of(uaa.findUser(intent.properties.email)));
	}


	@Override
	public void converge()
	{
		if(! intent.getResolution().exists())
		{
			String id = uaa.createUser(intent.properties);
			intent.setResolution(IdentifiableResolution.of(id));
		}
	}
}
