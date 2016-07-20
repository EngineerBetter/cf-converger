package com.engineerbetter.converger.facade;

import com.engineerbetter.converger.intents.OrgHandler;
import com.engineerbetter.converger.intents.OrgIntent;
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
		if(! intent.getResolution().exists())
		{
			cf.createOrg(intent.name.name);
		}
	}

}
