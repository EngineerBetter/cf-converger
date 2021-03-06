package com.engineerbetter.converger.model;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Org
{
	public final String name;
	public final List<Space> spaces;
	public final List<String> managers;
	public final List<String> auditors;

	@JsonCreator
	public Org(
			@JsonProperty("name") String name,
			@JsonProperty("spaces") List<Space> spaces,
			@JsonProperty("org_managers") List<String> managers,
			@JsonProperty("org_auditors") List<String> auditors
			)
	{
		this.name = name;
		this.spaces = Collections.unmodifiableList(spaces);
		this.managers = Collections.unmodifiableList(managers);
		this.auditors = Collections.unmodifiableList(auditors);
	}
}