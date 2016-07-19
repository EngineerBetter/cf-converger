package com.engineerbetter.converger;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.engineerbetter.converger.ConvergerSmokeTest.SmokeTestConfig;
import com.engineerbetter.converger.facade.CloudFoundryFacade;
import com.engineerbetter.converger.facade.UaaFacade;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes={ConvergerApplication.class, SmokeTestConfig.class})
@WebIntegrationTest(randomPort = true)
@ActiveProfiles("test")
public class ConvergerSmokeTest
{
	@Value("${local.server.port}")
	private int port;
	private RestTemplate rest = new TestRestTemplate();

	@Test
	public void appRespondsOnRootPath()
	{
		ResponseEntity<String> response = rest.getForEntity("http://127.0.0.1:"
				+ port, String.class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
	}

	public static class SmokeTestConfig
	{
		@Bean
		CloudFoundryFacade mockCf()
		{
			return Mockito.mock(CloudFoundryFacade.class);
		}

		@Bean
		UaaFacade mockUaa()
		{
			return Mockito.mock(UaaFacade.class);
		}
	}
}
