package com.engineerbetter.converger.facade;

import java.util.Optional;

import org.cloudfoundry.uaa.UaaClient;
import org.springframework.beans.factory.annotation.Autowired;

public class ReactorUaaClientFacade implements UaaFacade
{
	private final UaaClient uaa;

	@Autowired
	public ReactorUaaClientFacade(UaaClient uaa)
	{
		this.uaa = uaa;
	}

	@Override
	public Optional<String> findUser(String username)
	{
		return null;
	}

	@Override
	public String createUser(String username)
	{
		return null;
	}

	@Override
	public void deleteUser()
	{
	}

}
