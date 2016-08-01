package com.engineerbetter.converger;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.cloudfoundry.reactor.uaa.ReactorUaaClient;
import org.cloudfoundry.uaa.UaaClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.engineerbetter.converger.facade.CloudFoundryFacade;
import com.engineerbetter.converger.facade.ReactorCfClientFacade;
import com.engineerbetter.converger.facade.ReactorUaaClientFacade;
import com.engineerbetter.converger.facade.UaaFacade;
import com.engineerbetter.converger.facade.ops.ModifyingUserOps;

@Configuration
@Profile("!test")
public class CfConfig
{
	@Bean
	DefaultConnectionContext connectionContext(@Value("${cf.host}") String apiHost) {
		return DefaultConnectionContext.builder()
				.apiHost(apiHost)
				.skipSslValidation(true)
				.build();
	}

	@Bean
	PasswordGrantTokenProvider tokenProvider(@Value("${cf.username}") String username, @Value("${cf.password}") String password) {
		return PasswordGrantTokenProvider.builder()
				.password(password)
				.username(username)
				.build();
	}

	@Bean ModifyingUserOps createUserOps(ConnectionContext connectionContext, TokenProvider tokenProvider)
	{
		return new ModifyingUserOps(connectionContext, connectionContext.getRoot(), tokenProvider);
	}

	@Bean
	CloudFoundryClient cloudFoundryClient(ConnectionContext connectionContext, TokenProvider tokenProvider)
	{
		return ReactorCloudFoundryClient.builder()
				.connectionContext(connectionContext)
				.tokenProvider(tokenProvider)
				.build();
	}

	@Bean
	UaaClient uaaClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
		return ReactorUaaClient.builder()
				.connectionContext(connectionContext)
				.tokenProvider(tokenProvider)
				.build();
	}

	@Bean
	CloudFoundryFacade cfFacade(CloudFoundryClient cfClient, ModifyingUserOps createUserOps)
	{
		return new ReactorCfClientFacade(cfClient, createUserOps);
	}

	@Bean
	UaaFacade uaaFacade(UaaClient uaaClient)
	{
		return new ReactorUaaClientFacade(uaaClient);
	}
}
