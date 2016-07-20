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
		intent.setResolution(IdentifiableResolution.of(uaa.findUser(intent.name.name)));
	}


	@Override
	public void converge()
	{
	}
}
