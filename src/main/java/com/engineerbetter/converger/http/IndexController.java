package com.engineerbetter.converger.http;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.engineerbetter.converger.intents.Handler;
import com.engineerbetter.converger.intents.Intent;
import com.engineerbetter.converger.intents.OrderedHandlerBuilder;
import com.engineerbetter.converger.model.Declaration;
import com.engineerbetter.converger.resolution.Resolution;

@RestController
public class IndexController
{
	private final OrderedHandlerBuilder orderedIntentBuilder;

	@Autowired
	public IndexController(OrderedHandlerBuilder orderedIntentBuilder)
	{
		this.orderedIntentBuilder = orderedIntentBuilder;
	}

	@RequestMapping("/")
	public String index()
	{
		return "Up";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/", consumes = "application/x-yaml")
	public String upload(@RequestBody Declaration declaration) throws Exception
	{
		List<Handler<? extends Intent<? extends Resolution>>> handlers = orderedIntentBuilder.getOrderedHandlers(declaration);
		for(Handler<? extends Intent<? extends Resolution>> handler : handlers)
		{
			handler.resolve();
		}
		return "Converged org "+declaration.org.name;
	}
}
