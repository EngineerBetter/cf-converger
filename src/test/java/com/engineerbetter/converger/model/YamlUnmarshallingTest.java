package com.engineerbetter.converger.model;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class YamlUnmarshallingTest
{
	@Test
	public void unmarshallsCorrectly() throws Exception
	{
		ClassPathResource resource = new ClassPathResource("fixtures/declaration.yml");
		YAMLMapper mapper = new YAMLMapper();
		Declaration declaration = mapper.readValue(resource.getFile(), Declaration.class);

		assertThat(declaration.version, is(1));
		assertThat(declaration.org.name, is("my-lovely-org"));
	}
}
