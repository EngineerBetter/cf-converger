package com.engineerbetter.converger.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Declaration
{
	public final int version;
	public final Org org;

	@JsonCreator
	public Declaration(
			@JsonProperty("version") int version,
			@JsonProperty("org") Org org
			) {
		this.version = version;
		this.org = org;
	}
}
