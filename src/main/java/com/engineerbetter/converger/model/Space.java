package com.engineerbetter.converger.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Space
{
	public final String name;
	public final List<Ups> upss;
	public final List<String> developers;

	@JsonCreator
	public Space(
			@JsonProperty("name") String name,
			@JsonProperty(value="user_provided_services") List<Ups> upss,
			@JsonProperty("space_developers") List<String> developers
			)
	{
		this.name = name;
		this.upss = upss == null ? new ArrayList<>() : Collections.unmodifiableList(upss);
		this.developers = developers == null ? new ArrayList<>() : Collections.unmodifiableList(developers);
	}
}
