package com.engineerbetter.converger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.engineerbetter.converger.facade.CfFacadeCfUserHandlerBuilder;
import com.engineerbetter.converger.facade.CfFacadeOrgAuditorHandlerBuilder;
import com.engineerbetter.converger.facade.CfFacadeOrgHandlerBuilder;
import com.engineerbetter.converger.facade.CfFacadeOrgManagerHandlerBuilder;
import com.engineerbetter.converger.facade.CfFacadeSpaceDeveloperHandlerBuilder;
import com.engineerbetter.converger.facade.CfFacadeSpaceHandlerBuilder;
import com.engineerbetter.converger.facade.CfFacadeSpaceManagerHandlerBuilder;
import com.engineerbetter.converger.facade.CfFacadeUpsHandlerBuilder;
import com.engineerbetter.converger.facade.CfFacadeUserOrgHandlerBuilder;
import com.engineerbetter.converger.facade.CloudFoundryFacade;
import com.engineerbetter.converger.facade.UaaFacade;
import com.engineerbetter.converger.facade.UaaFacadeUaaUserHandlerBuilder;
import com.engineerbetter.converger.intents.CfUserIntent;
import com.engineerbetter.converger.intents.HandlerBuilder;
import com.engineerbetter.converger.intents.HandlerFactory;
import com.engineerbetter.converger.intents.OrgAuditorIntent;
import com.engineerbetter.converger.intents.OrgIntent;
import com.engineerbetter.converger.intents.OrgManagerIntent;
import com.engineerbetter.converger.intents.SpaceDeveloperIntent;
import com.engineerbetter.converger.intents.SpaceIntent;
import com.engineerbetter.converger.intents.SpaceManagerIntent;
import com.engineerbetter.converger.intents.TypeReferenceMapHandlerFactory;
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
		handlerFactory.put(new TypeReference<HandlerBuilder<OrgAuditorIntent>>() {}, new CfFacadeOrgAuditorHandlerBuilder(cf));
		handlerFactory.put(new TypeReference<HandlerBuilder<SpaceIntent>>() {}, new CfFacadeSpaceHandlerBuilder(cf));
		handlerFactory.put(new TypeReference<HandlerBuilder<SpaceDeveloperIntent>>() {}, new CfFacadeSpaceDeveloperHandlerBuilder(cf));
		handlerFactory.put(new TypeReference<HandlerBuilder<SpaceManagerIntent>>() {}, new CfFacadeSpaceManagerHandlerBuilder(cf));
		handlerFactory.put(new TypeReference<HandlerBuilder<UpsIntent>>() {}, new CfFacadeUpsHandlerBuilder(cf));
		return handlerFactory;
	}
}
