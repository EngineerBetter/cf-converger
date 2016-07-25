package com.engineerbetter.converger.facade;

import java.util.Optional;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;

import com.engineerbetter.converger.intents.UpsHandler;
import com.engineerbetter.converger.intents.UpsIntent;
import com.engineerbetter.converger.properties.UpsProperties;
import com.engineerbetter.converger.resolution.MutableResolution;

public class CfFacadeUpsHandler extends UpsHandler
{
	private final CloudFoundryFacade cf;


	public CfFacadeUpsHandler(UpsIntent intent, CloudFoundryFacade cf)
	{
		super(intent);
		this.cf = cf;
	}

	@Override
	public void resolve()
	{
		Optional<String> spaceId = intent.spaceIntent.getResolution().getId();

		if(spaceId.isPresent())
		{
			Optional<String> upsId = cf.findUps(intent.upsProperties.name, spaceId.get());

			if(upsId.isPresent())
			{
				UpsProperties observed = cf.getUps(upsId.get());
				if(intent.upsProperties.equals(observed))
				{
					intent.setResolution(MutableResolution.same(upsId.get()));
				}
				else
				{
					Javers javers = JaversBuilder.javers().build();
					Diff diff = javers.compare(observed, intent.upsProperties);
					intent.setResolution(MutableResolution.different(upsId.get(), diff.getChanges()));
				}
			}
			else
			{
				intent.setResolution(MutableResolution.absent());
			}
		}
		else
		{
			intent.setResolution(MutableResolution.absent());
		}
	}

	@Override
	public void converge()
	{
		if(! intent.getResolution().exists())
		{
			String spaceId = intent.spaceIntent.getResolution().getId().get();
			String upsId = cf.createUps(intent.upsProperties, spaceId);
			intent.setResolution(MutableResolution.same(upsId));
		}
		else if(intent.getResolution().hasDifferences())
		{
			String spaceId = intent.spaceIntent.getResolution().getId().get();
			cf.updateUps(intent.upsProperties, spaceId);
		}
	}

}
