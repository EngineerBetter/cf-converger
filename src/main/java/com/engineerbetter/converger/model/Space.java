package com.engineerbetter.converger.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Space
{
	public final String name;
	public final List<String> developers;

	@JsonCreator
	public Space(
			@JsonProperty("name") String name,
			@JsonProperty("space_developers") List<String> developers
			)
	{
		this.name = name;
		this.developers = developers == null ? new ArrayList<>() : Collections.unmodifiableList(developers);
	}
}
