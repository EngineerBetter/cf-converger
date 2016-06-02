package com.engineerbetter.converger.intents;

import com.engineerbetter.converger.facade.UaaFacade;
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
