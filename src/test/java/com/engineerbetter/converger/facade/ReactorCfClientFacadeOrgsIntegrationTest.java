package com.engineerbetter.converger.facade;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Optional;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.cloudfoundry.spring.client.SpringCloudFoundryClient;
import org.junit.Before;
import org.junit.Test;

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

		cfClient = SpringCloudFoundryClient.builder().host(host).username(username).password(password).skipSslValidation(true).build();
		cfOps = DefaultCloudFoundryOperations.builder().cloudFoundryClient(cfClient).build();
		facade = new ReactorCfClientFacade(cfClient);
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
