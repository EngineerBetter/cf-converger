package com.engineerbetter.converger;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v2.organizations.DeleteOrganizationRequest;
import org.cloudfoundry.client.v2.organizations.ListOrganizationsRequest;
import org.cloudfoundry.client.v2.organizations.ListOrganizationsResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.engineerbetter.converger.facade.CloudFoundryFacade;
import com.engineerbetter.converger.facade.CloudFoundryFacade.OrgRole;
import com.engineerbetter.converger.facade.CloudFoundryFacade.SpaceRole;
import com.engineerbetter.converger.facade.UaaFacade;
import com.engineerbetter.converger.properties.UpsProperties;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ConvergerApplication.class)
@WebIntegrationTest(randomPort=true)
public class UploadIntegrationTest {
	@Value("${local.server.port}")
	private int port;
	private RestTemplate rest = new TestRestTemplate();
	@Autowired
	private CloudFoundryClient cfClient;
	@Autowired
	private CloudFoundryFacade cfFacade;
	@Autowired
	private UaaFacade uaaFacade;

	@Before
	public void setup()
	{
		String orgId = cfFacade.createOrg("my-lovely-org");
		String prodId = cfFacade.createSpace("DEV", orgId);
		Map<String, String> credentials = new HashMap<>();
		credentials.put("username", "sa");
		credentials.put("password", "oldpassword");
		credentials.put("secret", "notneeded");
		UpsProperties upsProperties = new UpsProperties("OracleDB", credentials);
		cfFacade.createUps(upsProperties, prodId);
	}

	@After
	public void teardown() {
		ListOrganizationsResponse response = cfClient.organizations().list(ListOrganizationsRequest.builder().name("my-lovely-org").build()).block();

		if(response.getResources().size() > 0)
		{
			String orgId = response.getResources().get(0).getMetadata().getId();
			cfClient.organizations().delete(DeleteOrganizationRequest.builder().organizationId(orgId).recursive(true).build()).block();
		}

		deleteUser("dan.young@example.com");
		deleteUser("daniel.jones@example.com");
	}

	@Test
	public void converge() throws Exception {
		ClassPathResource fixture = new ClassPathResource("fixtures/declaration.yml");
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("Content-Type", "application/x-yaml");
		requestHeaders.add("Accept", "application/json");

		URI uri = new URI("http://127.0.0.1:"+port);
		byte[] bytes = new byte[(int)fixture.contentLength()];
		fixture.getInputStream().read(bytes);
		RequestEntity<byte[]> postRequest = new RequestEntity<byte[]>(bytes, requestHeaders, HttpMethod.POST, uri);

		ResponseEntity<String> response = rest.exchange(postRequest, String.class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody(), is("Converged org my-lovely-org"));

		Optional<String> dyId = uaaFacade.findUser("dan.young@example.com");
		assertThat("dan.young@example.com should exist in UAA", dyId.isPresent(), is(true));

		Optional<String> orgId = cfFacade.findOrg("my-lovely-org");
		assertThat("my-lovely-org should exist", orgId.isPresent(), is(true));

		assertThat("dan.young@example should be in my-lovely-org", cfFacade.isUserInOrg(dyId.get(), orgId.get()), is(true));
		assertThat("dan.young@example should be manager of my-lovely-org", cfFacade.hasOrgRole(dyId.get(), orgId.get(), OrgRole.MANAGER), is(true));
		assertThat("dan.young@example should be auditor of my-lovely-org", cfFacade.hasOrgRole(dyId.get(), orgId.get(), OrgRole.AUDITOR), is(true));

		Optional<String> devId = cfFacade.findSpace("DEV", orgId.get());
		assertThat("DEV should exist", devId.isPresent(), is(true));

		Optional<String> djId = uaaFacade.findUser("daniel.jones@example.com");
		assertThat("daniel.jones@example.com should exist in UAA", djId.isPresent(), is(true));
		assertThat("daniel.jones@example.com should be DEV space manager", cfFacade.hasSpaceRole(djId.get(), devId.get(), SpaceRole.AUDITOR), is(true));
		assertThat("daniel.jones@example.com should be DEV space developer", cfFacade.hasSpaceRole(djId.get(), devId.get(), SpaceRole.DEVELOPER), is(true));
		assertThat("daniel.jones@example.com should be DEV space manager", cfFacade.hasSpaceRole(djId.get(), devId.get(), SpaceRole.MANAGER), is(true));

		Optional<String> oracleDbId = cfFacade.findUps("OracleDB", devId.get());
		assertThat("OracleDB should exist", oracleDbId.isPresent(), is(true));
		Map<String, String> credentials = new HashMap<>();
		credentials.put("username", "sa");
		credentials.put("password", "secret");
		UpsProperties oracleDb = new UpsProperties("OracleDB", credentials);
		assertThat("OracleDB should have been updated", cfFacade.getUps(oracleDbId.get()), equalTo(oracleDb));

		assertThat("PROD should exist", cfFacade.findSpace("PROD", orgId.get()).isPresent(), is(true));
	}

	@Test
	public void plan() throws Exception {
		ClassPathResource fixture = new ClassPathResource("fixtures/declaration.yml");
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("Content-Type", "application/x-yaml");
		requestHeaders.add("Accept", "application/json");

		URI uri = new URI("http://127.0.0.1:"+port+"/plan");
		byte[] bytes = new byte[(int)fixture.contentLength()];
		fixture.getInputStream().read(bytes);
		RequestEntity<byte[]> postRequest = new RequestEntity<byte[]>(bytes, requestHeaders, HttpMethod.POST, uri);

		ResponseEntity<List<String>> response = rest.exchange(postRequest, new ParameterizedTypeReference<List<String>>(){});
		assertThat(response.getBody(), hasItem("Would create UaaUserIntent [properties=UaaUserProperties [email=dan.young@example.com, givenName=Dan, familyName=Young]]"));
		assertThat(response.getBody(), hasItem("Would not create OrgIntent [name=my-lovely-org]"));
		assertThat(response.getBody(), hasItem("Would set dan.young@example.com as manager of my-lovely-org"));
		assertThat(response.getBody(), hasItem("Would set dan.young@example.com as auditor of my-lovely-org"));
		assertThat(response.getBody(), hasItem(containsString("Would not create SpaceIntent [name=DEV")));
		assertThat(response.getBody(), hasItem("Would set daniel.jones@example.com as auditor of DEV"));
		assertThat(response.getBody(), hasItem("Would set daniel.jones@example.com as developer of DEV"));
		assertThat(response.getBody(), hasItem("Would set daniel.jones@example.com as manager of DEV"));
		assertThat(response.getBody(), hasItem(containsString("Would create SpaceIntent [name=PROD")));
		assertThat(response.getBody(), hasItem(containsString("Would update UpsIntent, changing entry password from oldpassword to secret in credentials, and removing entry secret->notneeded from credentials")));
	}

	private void deleteUser(String email)
	{
		Optional<String> id = uaaFacade.findUser(email);
		if(id.isPresent())
		{
			uaaFacade.deleteUser(id.get());
		}
	}
}
