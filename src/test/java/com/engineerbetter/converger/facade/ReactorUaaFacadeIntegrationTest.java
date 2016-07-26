package com.engineerbetter.converger.facade;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Optional;

import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.cloudfoundry.reactor.uaa.ReactorUaaClient;
import org.junit.Before;
import org.junit.Test;

public class ReactorUaaFacadeIntegrationTest
{
	private UaaFacade uaa;


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

		ReactorUaaClient uaaClient = ReactorUaaClient.builder().connectionContext(connectionContext).tokenProvider(tokenProvider).build();
		uaa = new ReactorUaaClientFacade(uaaClient);
	}


	@Test
	public void usersManagement()
	{
		Optional<String> id = uaa.findUser(System.getenv("CF_USERNAME"));
		assertThat("User used to run tests should exist in the UAA", id.isPresent(), is(true));
		assertThat(id.get(), not(""));

		String createdId = uaa.createUser("cf-converger-test-user");
		assertThat(createdId, notNullValue());
		Optional<String> foundId = uaa.findUser("cf-converger-test-user");
		assertThat("User that test created should be findable", foundId.isPresent(), is(true));

		uaa.deleteUser(foundId.get());

		foundId = uaa.findUser("cf-converger-test-user");
		assertThat("User that test deleted should not be findable", foundId.isPresent(), is(false));
	}
}
