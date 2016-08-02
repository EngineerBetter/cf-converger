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
	public final List<String> auditors;
	public final List<String> developers;
	public final List<String> managers;

	@JsonCreator
	public Space(
			@JsonProperty("name") String name,
			@JsonProperty(value="user_provided_services") List<Ups> upss,
			@JsonProperty("space_auditors") List<String> auditors,
			@JsonProperty("space_developers") List<String> developers,
			@JsonProperty("space_managers") List<String> managers
			)
	{
		this.name = name;
		this.upss = upss == null ? new ArrayList<>() : Collections.unmodifiableList(upss);
		this.auditors = auditors == null ? new ArrayList<>() : Collections.unmodifiableList(auditors);
		this.developers = developers == null ? new ArrayList<>() : Collections.unmodifiableList(developers);
		this.managers = managers == null ? new ArrayList<>() : Collections.unmodifiableList(managers);
	}
}
