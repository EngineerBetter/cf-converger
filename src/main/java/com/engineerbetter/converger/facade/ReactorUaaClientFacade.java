package com.engineerbetter.converger.facade;

import java.util.Optional;

import org.cloudfoundry.uaa.UaaClient;
import org.cloudfoundry.uaa.users.ListUsersRequest;
import org.cloudfoundry.uaa.users.ListUsersResponse;
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
		ListUsersResponse response = uaa.users().list(ListUsersRequest.builder().filter("username eq \""+username+"\"").build()).block();

		if(response.getTotalResults() > 0)
		{
			return Optional.of(response.getResources().get(0).getId());
		}

		return Optional.empty();
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
