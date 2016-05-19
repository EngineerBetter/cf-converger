package com.engineerbetter.converger;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ConvergerApplication.class)
@WebIntegrationTest(randomPort=true)
public class ConvergerSmokeTest {
	@Value("${local.server.port}")
	private int port;

	@Test
	public void appRespondsOnRootPath()	{
		RestTemplate rest = new TestRestTemplate();
		ResponseEntity<String> response = rest.getForEntity("http://127.0.0.1:"+port, String.class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
	}
}
