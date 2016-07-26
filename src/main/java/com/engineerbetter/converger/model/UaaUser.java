package com.engineerbetter.converger.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UaaUser
{
	public final String email;
	public final String givenName;
	public final String familyName;

	@JsonCreator
	public UaaUser(
			@JsonProperty("email") String email,
			@JsonProperty("givenName") String givenName,
			@JsonProperty("familyName") String familyName)
	{
		this.email = email;
		this.givenName = givenName;
		this.familyName = familyName;
	}
}
