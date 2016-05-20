package com.engineerbetter.converger.http;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class YamlHttpConfig extends WebMvcConfigurerAdapter
{
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters)
	{
		converters.add(new Jackson2YamlHttpMessageConverter());
	}
}
