package com.engineerbetter.converger.facade;

import java.util.Optional;

import com.engineerbetter.converger.properties.UaaUserProperties;

public interface UaaFacade
{
	Optional<String> findUser(String email);

	String createUser(UaaUserProperties properties);

	void deleteUser(String userId);
}
