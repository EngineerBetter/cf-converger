package com.engineerbetter.converger.facade;

import java.util.Optional;

public interface UaaFacade
{
	Optional<String> findUser(String username);

	String createUser(String username);

	void deleteUser();
}
