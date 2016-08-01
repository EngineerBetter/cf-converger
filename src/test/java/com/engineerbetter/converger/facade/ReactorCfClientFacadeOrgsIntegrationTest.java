package com.engineerbetter.converger.facade;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.junit.Before;
import org.junit.Test;

import com.engineerbetter.converger.facade.ops.ModifyingUserOps;

public class ReactorCfClientFacadeOrgsIntegrationTest
{
	private CloudFoundryClient cfClient;
	private CloudFoundryOperations cfOps;
	private CloudFoundryFacade facade;

	@Before
	public void setup()
	{
		String host = System.getenv("CF_HOST");
		String username = System.getenv("CF_USERNAME");
		String password = System.getenv("CF_PASSWORD");

		if(host == null || username == null || password == null)
		{
			throw new RuntimeException("CF_HOST, CF_USERNAME and CF_PASSWORD must be set");
		}

		ConnectionContext connectionContext = DefaultConnectionContext.builder().apiHost(host).skipSslValidation(true).build();
		TokenProvider tokenProvider = PasswordGrantTokenProvider.builder().username(username).password(password).build();
		cfClient = ReactorCloudFoundryClient.builder().connectionContext(connectionContext).tokenProvider(tokenProvider).build();
		cfOps = DefaultCloudFoundryOperations.builder().cloudFoundryClient(cfClient).build();
		facade = new ReactorCfClientFacade(cfClient, mock(ModifyingUserOps.class));
	}


	@Test
	public void orgManagement()
	{
		assertThat(cfOps.organizations().list().filter(o -> o.getName().equals("test-org")).count().block(), is(0L));
		String id = facade.createOrg("test-org");
		assertThat(cfOps.organizations().list().filter(o -> o.getName().equals("test-org")).count().block(), is(1L));
		assertThat(facade.findOrg("test-org"), is(Optional.of(id)));
		facade.deleteOrg(id);
		assertThat(cfOps.organizations().list().filter(o -> o.getName().equals("test-org")).count().block(), is(0L));
	}
}
