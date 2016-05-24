package com.engineerbetter.converger.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Declaration
{
	public static final int SCHEMA_VERSION = 1;
	public final int version;
	public final Org org;

	@JsonCreator
	public Declaration(
			@JsonProperty("schema_version") int version,
			@JsonProperty("org") Org org
			) {
		this.version = version;
		this.org = org;
	}
}
