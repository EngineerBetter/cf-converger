package com.engineerbetter.converger.model;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.engineerbetter.converger.HandlerConfig;
import com.engineerbetter.converger.facade.CloudFoundryFacade;
import com.engineerbetter.converger.facade.UaaFacade;
import com.engineerbetter.converger.intents.CfUserIntent;
import com.engineerbetter.converger.intents.Handler;
import com.engineerbetter.converger.intents.HandlerFactory;
import com.engineerbetter.converger.intents.HardcodedOrderedHandlerBuilder;
import com.engineerbetter.converger.intents.Intent;
import com.engineerbetter.converger.intents.OrderedHandlerBuilder;
import com.engineerbetter.converger.intents.OrgIntent;
import com.engineerbetter.converger.intents.OrgManagerIntent;
import com.engineerbetter.converger.intents.SpaceDeveloperIntent;
import com.engineerbetter.converger.intents.SpaceIntent;
import com.engineerbetter.converger.intents.SpaceManagerIntent;
import com.engineerbetter.converger.intents.UaaUserIntent;
import com.engineerbetter.converger.intents.UserOrgIntent;
import com.engineerbetter.converger.properties.NameProperty;
import com.engineerbetter.converger.properties.UaaUserProperties;
import com.engineerbetter.converger.resolution.Resolution;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class HardcodedOrderedIntentBuilderTest
{
	private Declaration fixture;
	private HandlerFactory handlerFactory;
	private OrderedHandlerBuilder builder;

	@Before
	public void setup() throws Exception
	{
		YAMLMapper mapper = new YAMLMapper();
		ClassPathResource resource = new ClassPathResource("fixtures/declaration.yml");
		fixture = mapper.readValue(resource.getFile(), Declaration.class);

		CloudFoundryFacade cf = mock(CloudFoundryFacade.class);
		UaaFacade uaa = mock(UaaFacade.class);

		handlerFactory = new HandlerConfig().handlerFactory(cf, uaa);
		builder = new HardcodedOrderedHandlerBuilder(handlerFactory);
	}

	@Test
	public void testAllTheThings()
	{
		List<Handler<? extends Intent<? extends Resolution>>> handlers = builder.getOrderedHandlers(fixture);
		OrgIntent org = new OrgIntent(new NameProperty("my-lovely-org"));
		Handler<OrgIntent> orgHandler = handlerFactory.build(org);
		SpaceIntent devSpace = new SpaceIntent(new NameProperty("DEV"), org);
		Handler<SpaceIntent> devSpaceHandler = handlerFactory.build(devSpace);
		SpaceIntent prodSpace = new SpaceIntent(new NameProperty("PROD"), org);
		Handler<SpaceIntent> prodSpaceHandler = handlerFactory.build(prodSpace);
		assertThat(handlers, hasItem(orgHandler));
		assertThisAppearsBeforeThat(orgHandler, devSpaceHandler, handlers);
		assertThisAppearsBeforeThat(orgHandler, prodSpaceHandler, handlers);

		UaaUserIntent uaaDanYoung = new UaaUserIntent(new UaaUserProperties("dan.young@example.com", "Dan", "Young"));
		Handler<UaaUserIntent> uaaDanYoungHandler = handlerFactory.build(uaaDanYoung);
		UaaUserIntent uaaDanJones = new UaaUserIntent(new UaaUserProperties("daniel.jones@example.com", "Daniel", "Jones"));
		Handler<UaaUserIntent> uaaDanJonesHandler = handlerFactory.build(uaaDanJones);

		CfUserIntent cfDanYoung = new CfUserIntent(uaaDanYoung);
		Handler<CfUserIntent> cfDanYoungHandler = handlerFactory.build(cfDanYoung);
		CfUserIntent cfDanJones = new CfUserIntent(uaaDanJones);
		Handler<CfUserIntent> cfDanJonesHandler = handlerFactory.build(cfDanJones);
		assertThisAppearsBeforeThat(uaaDanYoungHandler, cfDanYoungHandler, handlers);
		assertThisAppearsBeforeThat(uaaDanJonesHandler, cfDanJonesHandler, handlers);

		UserOrgIntent danYoungOrg = new UserOrgIntent(org, cfDanYoung);
		Handler<UserOrgIntent> danYoungOrgHandler = handlerFactory.build(danYoungOrg);
		UserOrgIntent danJonesOrg = new UserOrgIntent(org, cfDanJones);
		Handler<UserOrgIntent> danJonesOrgHandler = handlerFactory.build(danJonesOrg);
		assertThisAppearsBeforeThat(orgHandler, danYoungOrgHandler, handlers);
		assertThisAppearsBeforeThat(cfDanYoungHandler, danYoungOrgHandler, handlers);
		assertThisAppearsBeforeThat(orgHandler, danJonesOrgHandler, handlers);
		assertThisAppearsBeforeThat(cfDanJonesHandler, danJonesOrgHandler, handlers);

		OrgManagerIntent orgManager = new OrgManagerIntent(org, cfDanYoung);
		Handler<OrgManagerIntent> orgManagerHandler = handlerFactory.build(orgManager);
		assertThisAppearsBeforeThat(orgHandler, orgManagerHandler, handlers);
		assertThisAppearsBeforeThat(cfDanYoungHandler, orgManagerHandler, handlers);

		SpaceDeveloperIntent devSpaceDeveloper = new SpaceDeveloperIntent(devSpace, cfDanJones);
		Handler<SpaceDeveloperIntent> devSpaceDeveloperHandler = handlerFactory.build(devSpaceDeveloper);
		assertThisAppearsBeforeThat(devSpaceHandler, devSpaceDeveloperHandler, handlers);
		assertThisAppearsBeforeThat(danJonesOrgHandler, devSpaceDeveloperHandler, handlers);

		SpaceManagerIntent devSpaceManager = new SpaceManagerIntent(devSpace, cfDanJones);
		Handler<SpaceManagerIntent> devSpaceManagerHandler = handlerFactory.build(devSpaceManager);
		assertThisAppearsBeforeThat(devSpaceHandler, devSpaceManagerHandler, handlers);
		assertThisAppearsBeforeThat(danJonesOrgHandler, devSpaceManagerHandler, handlers);

		SpaceDeveloperIntent prodSpaceDeveloper = new SpaceDeveloperIntent(prodSpace, cfDanJones);
		Handler<SpaceDeveloperIntent> prodSpaceDeveloperHandler = handlerFactory.build(prodSpaceDeveloper);
		assertThisAppearsBeforeThat(prodSpaceHandler, prodSpaceDeveloperHandler, handlers);
		assertThisAppearsBeforeThat(danJonesOrgHandler, prodSpaceDeveloperHandler, handlers);
	}


	private void assertThisAppearsBeforeThat(Handler<? extends Intent<? extends Resolution>> before, Handler<? extends Intent<? extends Resolution>> after, List<Handler<? extends Intent<? extends Resolution>>> handlers)
	{
		assertThat(handlers, hasItem(before));
		assertThat(handlers, hasItem(after));

		int beforeIndex = -1;
		int afterIndex = -1;

		for(int index = 0; index < handlers.size(); index++)
		{
			Handler<? extends Intent<? extends Resolution>> current = handlers.get(index);
			if(current.equals(before))
			{
				beforeIndex = index;
			}

			if(current.equals(after))
			{
				afterIndex = index;
			}
		}

		assertThat("Handler did not appear in list", beforeIndex, not(-1));
		assertThat("Handler did not appear in list", afterIndex, not(-1));
		assertThat("Handlers appeared to be the same", beforeIndex, not(afterIndex));
		assertThat(before+" did not appear before "+after, beforeIndex, lessThan(afterIndex));
	}
}
