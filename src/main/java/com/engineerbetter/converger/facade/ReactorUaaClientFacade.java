package com.engineerbetter.converger.facade;

import java.util.Optional;

import org.cloudfoundry.uaa.UaaClient;
import org.cloudfoundry.uaa.users.CreateUserRequest;
import org.cloudfoundry.uaa.users.CreateUserResponse;
import org.cloudfoundry.uaa.users.DeleteUserRequest;
import org.cloudfoundry.uaa.users.Email;
import org.cloudfoundry.uaa.users.ListUsersRequest;
import org.cloudfoundry.uaa.users.ListUsersResponse;
import org.cloudfoundry.uaa.users.Name;
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
	public String createUser(String email)
	{
		Email emailInstance = Email.builder().value(email).primary(true).build();
		Name name = Name.builder().givenName("GivenName").familyName("FamilyName").build();
		CreateUserResponse response = uaa.users().create(CreateUserRequest.builder().userName(email).password("password").name(name).email(emailInstance).build()).block();
		return response.getId();
	}

	@Override
	public void deleteUser(String userId)
	{
		uaa.users().delete(DeleteUserRequest.builder().userId(userId).build()).block();
	}

}
