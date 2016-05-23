package com.engineerbetter.converger.model;

import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Ups
{
	public final String name;
	public final Map<String, String> credentials;

	@JsonCreator
	public Ups(@JsonProperty("name") String name, @JsonProperty("credentials") Map<String, String> credentials)
	{
		this.name = name;
		this.credentials = Collections.unmodifiableMap(credentials);
	}
}