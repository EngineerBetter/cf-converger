package com.engineerbetter.converger.http;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class Jackson2YamlHttpMessageConverter extends AbstractJackson2HttpMessageConverter
{
	public Jackson2YamlHttpMessageConverter()
	{
		super(new YAMLMapper(), MediaType.parseMediaType("application/x-yaml"));
	}
}