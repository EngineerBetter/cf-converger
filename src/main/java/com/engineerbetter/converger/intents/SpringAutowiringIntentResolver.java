package com.engineerbetter.converger.intents;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class SpringAutowiringIntentResolver implements IntentResolver
{
	private final ApplicationContext applicationContext;

	@Autowired
	public SpringAutowiringIntentResolver(ApplicationContext applicationContext)
	{
		this.applicationContext = applicationContext;
	}

	@Override
	public void resolve(List<Intent> intents)
	{
		//Find out if I can exist, and if I can, do I
		for(Intent intent : intents) {
			applicationContext.getAutowireCapableBeanFactory().autowireBean(intent);
			//I will have everything that I need because topological ordering
			intent.resolve();
		}
	}
}
