package com.engineerbetter.converger.facade;

import java.util.Optional;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v2.organizations.CreateOrganizationRequest;
import org.cloudfoundry.client.v2.organizations.CreateOrganizationResponse;
import org.cloudfoundry.client.v2.organizations.DeleteOrganizationRequest;
import org.cloudfoundry.client.v2.organizations.ListOrganizationsRequest;
import org.cloudfoundry.client.v2.organizations.ListOrganizationsResponse;
import org.springframework.beans.factory.annotation.Autowired;

public class ReactorCfClientFacade implements CloudFoundryFacade
{
	private final CloudFoundryClient cf;

	@Autowired
	public ReactorCfClientFacade(CloudFoundryClient cf)
	{
		this.cf = cf;
	}

	@Override
	public Optional<String> findOrg(String name)
	{
		ListOrganizationsResponse response = cf.organizations().list(ListOrganizationsRequest.builder().name(name).build()).get();
		if(response.getResources().size() > 0)
		{
			return Optional.of(response.getResources().get(0).getMetadata().getId());
		}

		return Optional.empty();
	}

	@Override
	public String createOrg(String name)
	{
		CreateOrganizationResponse response = cf.organizations().create(CreateOrganizationRequest.builder().name(name).build()).get();
		return response.getMetadata().getId();
	}

	@Override
	public void deleteOrg(String id)
	{
		cf.organizations().delete(DeleteOrganizationRequest.builder().organizationId(id).recursive(true).build()).get();
	}

	@Override
	public Optional<String> findSpace(String name, String orgId)
	{
		return null;
	}

	@Override
	public String createSpace(String name, String orgId)
	{
		return null;
	}

	@Override
	public void deleteSpace(String id)
	{
	}

	@Override
	public Optional<String> findUps(String name, String spaceId)
	{
		return null;
	}

	@Override
	public String createUps()
	{
		return null;
	}

	@Override
	public void deleteUps(String id)
	{
	}

	@Override
	public boolean userExists(String id)
	{
		return false;
	}

	@Override
	public boolean hasOrgRole(String userId, String orgId, OrgRole role)
	{
		return false;
	}

	@Override
	public boolean hasSpaceRole(String userId, String spaceId, SpaceRole role)
	{
		return false;
	}

	@Override
	public boolean isUserInOrg(String userId, String orgId)
	{
		return false;
	}

}
