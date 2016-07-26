package com.engineerbetter.converger.facade;

import java.util.Optional;

public interface UaaFacade
{
	Optional<String> findUser(String email);

	String createUser(String email);

	void deleteUser(String userId);
}
