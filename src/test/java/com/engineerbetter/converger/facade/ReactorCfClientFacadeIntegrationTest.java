package com.engineerbetter.converger.facade;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v2.spaces.ListSpacesRequest;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.ListUserProvidedServiceInstancesRequest;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.engineerbetter.converger.properties.UpsProperties;

public class ReactorCfClientFacadeIntegrationTest
{
	private CloudFoundryClient cfClient;
	private CloudFoundryFacade facade;
	private String orgId;

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
		facade = new ReactorCfClientFacade(cfClient);

		String orgName = "converger-test-"+UUID.randomUUID().toString();
		orgId = facade.createOrg(orgName);
	}

	@After
	public void teardown()
	{
		facade.deleteOrg(orgId);
	}

	@Test
	public void spaceManagement()
	{
		assertThat(cfClient.spaces().list(ListSpacesRequest.builder().organizationId(orgId).name("test-space").build()).block().getResources().size(), is(0));
		String id = facade.createSpace("test-space", orgId);
		assertThat(cfClient.spaces().list(ListSpacesRequest.builder().organizationId(orgId).name("test-space").build()).block().getResources().size(), is(1));
		assertThat(facade.findSpace("test-space", orgId), is(Optional.of(id)));
		facade.deleteSpace(id);
		assertThat(cfClient.spaces().list(ListSpacesRequest.builder().organizationId(orgId).name("test-space").build()).block().getResources().size(), is(0));
	}

	@Test
	public void upsManagement()
	{
		String spaceId = facade.createSpace("test-space", orgId);
		assertThat(cfClient.userProvidedServiceInstances().list(ListUserProvidedServiceInstancesRequest.builder().spaceId(spaceId).name("test-ups").build()).block().getResources().size(), is(0));

		Map<String, String> credentials = new HashMap<>();
		credentials.put("username", "sa");
		credentials.put("password", "blank");
		UpsProperties upsProperties = new UpsProperties("test-ups", credentials);

		String id = facade.createUps(upsProperties, spaceId);
		assertThat(cfClient.userProvidedServiceInstances().list(ListUserProvidedServiceInstancesRequest.builder().spaceId(spaceId).name("test-ups").build()).block().getResources().size(), is(1));
		assertThat(facade.findUps("test-ups", spaceId), is(Optional.of(id)));
		assertThat(facade.getUps(id), equalTo(upsProperties));

		facade.deleteUps(id);
		assertThat(cfClient.userProvidedServiceInstances().list(ListUserProvidedServiceInstancesRequest.builder().spaceId(spaceId).name("test-ups").build()).block().getResources().size(), is(0));
	}
}
