package com.engineerbetter.converger.intents;

import java.util.Optional;

import com.engineerbetter.converger.facade.CloudFoundryFacade;
import com.engineerbetter.converger.resolution.IdentifiableResolution;

public class CfFacadeCfUserHandler extends CfUserHandler
{
	private final CloudFoundryFacade cf;

	public CfFacadeCfUserHandler(CfUserIntent intent, CloudFoundryFacade cf)
	{
		super(intent);
		this.cf = cf;
	}

	@Override
	public void resolve()
	{
		Optional<String> userId = intent.uaaUserIntent.getResolution().getId();

		if(userId.isPresent() && cf.userExists(userId.get()))
		{
			intent.setResolution(IdentifiableResolution.of(userId));
		}
		else
		{
			intent.setResolution(IdentifiableResolution.absent());
		}
	}

	@Override
	public void converge()
	{
	}

}
