package com.engineerbetter.converger.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Declaration
{
	public static final int SCHEMA_VERSION = 2;
	public final int schemaVersion;
	public final List<UaaUser> uaaUsers;
	public final Org org;

	@JsonCreator
	public Declaration(
			@JsonProperty("schema_version") int schemaVersion,
			@JsonProperty("uaa") List<UaaUser> uaaUsers,
			@JsonProperty("org") Org org
			)
	{
		this.schemaVersion = schemaVersion;
		this.uaaUsers = uaaUsers;
		this.org = org;
	}
}
