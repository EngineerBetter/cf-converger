package com.engineerbetter.converger.intents;

import com.engineerbetter.converger.facade.CloudFoundryFacade;
import com.engineerbetter.converger.resolution.IdentifiableResolution;

public class CfFacadeOrgHandler extends OrgHandler
{
	private final CloudFoundryFacade cf;

	public CfFacadeOrgHandler(OrgIntent intent, CloudFoundryFacade cf)
	{
		super(intent);
		this.cf = cf;
	}

	@Override
	public void resolve()
	{
		intent.setResolution(IdentifiableResolution.of(cf.findOrg(intent.name.name)));
	}

	@Override
	public void converge()
	{
	}

}
