package com.engineerbetter.converger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.engineerbetter.converger.facade.CloudFoundryFacade;
import com.engineerbetter.converger.facade.UaaFacade;
import com.engineerbetter.converger.intents.CfFacadeCfUserHandlerBuilder;
import com.engineerbetter.converger.intents.CfFacadeOrgHandlerBuilder;
import com.engineerbetter.converger.intents.CfFacadeOrgManagerHandlerBuilder;
import com.engineerbetter.converger.intents.CfFacadeSpaceDeveloperHandlerBuilder;
import com.engineerbetter.converger.intents.CfFacadeSpaceHandlerBuilder;
import com.engineerbetter.converger.intents.CfFacadeUpsHandlerBuilder;
import com.engineerbetter.converger.intents.CfFacadeUserOrgHandlerBuilder;
import com.engineerbetter.converger.intents.CfUserIntent;
import com.engineerbetter.converger.intents.HandlerBuilder;
import com.engineerbetter.converger.intents.HandlerFactory;
import com.engineerbetter.converger.intents.OrgIntent;
import com.engineerbetter.converger.intents.OrgManagerIntent;
import com.engineerbetter.converger.intents.SpaceDeveloperIntent;
import com.engineerbetter.converger.intents.SpaceIntent;
import com.engineerbetter.converger.intents.TypeReferenceMapHandlerFactory;
import com.engineerbetter.converger.intents.UaaFacadeUaaUserHandlerBuilder;
import com.engineerbetter.converger.intents.UaaUserIntent;
import com.engineerbetter.converger.intents.UpsIntent;
import com.engineerbetter.converger.intents.UserOrgIntent;
import com.fasterxml.jackson.core.type.TypeReference;

@Configuration
public class HandlerConfig
{
	@Bean
	public HandlerFactory handlerFactory(CloudFoundryFacade cf, UaaFacade uaa)
	{
		TypeReferenceMapHandlerFactory handlerFactory = new TypeReferenceMapHandlerFactory();
		handlerFactory.put(new TypeReference<HandlerBuilder<OrgIntent>>() {}, new CfFacadeOrgHandlerBuilder(cf));
		handlerFactory.put(new TypeReference<HandlerBuilder<UserOrgIntent>>() {}, new CfFacadeUserOrgHandlerBuilder(cf));
		handlerFactory.put(new TypeReference<HandlerBuilder<CfUserIntent>>() {}, new CfFacadeCfUserHandlerBuilder(cf));
		handlerFactory.put(new TypeReference<HandlerBuilder<UaaUserIntent>>() {}, new UaaFacadeUaaUserHandlerBuilder(uaa));
		handlerFactory.put(new TypeReference<HandlerBuilder<OrgManagerIntent>>() {}, new CfFacadeOrgManagerHandlerBuilder(cf));
		handlerFactory.put(new TypeReference<HandlerBuilder<SpaceIntent>>() {}, new CfFacadeSpaceHandlerBuilder(cf));
		handlerFactory.put(new TypeReference<HandlerBuilder<SpaceDeveloperIntent>>() {}, new CfFacadeSpaceDeveloperHandlerBuilder(cf));
		handlerFactory.put(new TypeReference<HandlerBuilder<UpsIntent>>() {}, new CfFacadeUpsHandlerBuilder(cf));
		return handlerFactory;
	}
}
