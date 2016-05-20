package com.engineerbetter.converger;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.operations.CloudFoundryOperationsBuilder;
import org.cloudfoundry.reactor.uaa.ReactorUaaClient;
import org.cloudfoundry.spring.client.SpringCloudFoundryClient;
import org.cloudfoundry.uaa.UaaClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class CfConfig
{
	@Bean
	CloudFoundryClient cloudFoundryClient(
			@Value("${cf.host}") String host,
			@Value("${cf.username}") String username,
			@Value("${cf.password}") String password)
	{
		return SpringCloudFoundryClient.builder().host(host).username(username)
				.password(password).skipSslValidation(true).build();
	}

	@Bean
	UaaClient uaaClient(SpringCloudFoundryClient cloudFoundryClient) {
		return ReactorUaaClient.builder().cloudFoundryClient(cloudFoundryClient).build();
	}

	@Bean
	CloudFoundryOperations cloudFoundryOperations(CloudFoundryClient cloudFoundryClient, UaaClient uaaClient) {
		return new CloudFoundryOperationsBuilder().cloudFoundryClient(cloudFoundryClient).uaaClient(uaaClient).build();
	}
}
