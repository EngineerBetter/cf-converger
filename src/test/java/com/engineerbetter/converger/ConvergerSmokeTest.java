package com.engineerbetter.converger;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ConvergerApplication.class)
@WebIntegrationTest(randomPort=true)
public class ConvergerSmokeTest {
	@Value("${local.server.port}")
	private int port;
	private RestTemplate rest = new TestRestTemplate();

	@Test
	public void appRespondsOnRootPath()	{
		ResponseEntity<String> response = rest.getForEntity("http://127.0.0.1:"+port, String.class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
	}

	@Test
	public void returnsTheSizeOfAUploadedYaml() throws URISyntaxException {
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
		parts.add("file", new ClassPathResource("declaration.yml"));

		HttpHeaders requestHeaders = new HttpHeaders();

		URI uri = new URI("http://127.0.0.1:"+port);

		ResponseEntity<String> response = rest.exchange(
				new RequestEntity<MultiValueMap<String, Object>>(parts, requestHeaders, HttpMethod.POST, uri),
				String.class
				);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody(), is("Size was: 30"));
	}
}
