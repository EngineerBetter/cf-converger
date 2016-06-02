package com.engineerbetter.converger.intents;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.engineerbetter.converger.properties.NameProperty;
import com.fasterxml.jackson.core.type.TypeReference;

public class TypeReferenceMapHandlerFactoryTest
{
	private TypeReferenceMapHandlerFactory factory;


	@Before
	public void setup()
	{
		factory = new TypeReferenceMapHandlerFactory();
		factory.put(new TypeReference<HandlerBuilder<OrgIntent>>() {}, new CfFacadeOrgHandlerBuilder());
	}

	@Test
	public void build()
	{
		Handler<OrgIntent> handler = factory.build(new OrgIntent(new NameProperty("geoff")));
		assertThat(handler, notNullValue());
	}
}
