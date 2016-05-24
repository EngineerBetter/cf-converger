package com.engineerbetter.converger.facade;

import java.util.Map;
import java.util.Optional;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v2.organizations.CreateOrganizationRequest;
import org.cloudfoundry.client.v2.organizations.CreateOrganizationResponse;
import org.cloudfoundry.client.v2.organizations.DeleteOrganizationRequest;
import org.cloudfoundry.client.v2.organizations.ListOrganizationAuditorsRequest;
import org.cloudfoundry.client.v2.organizations.ListOrganizationAuditorsResponse;
import org.cloudfoundry.client.v2.organizations.ListOrganizationManagersRequest;
import org.cloudfoundry.client.v2.organizations.ListOrganizationManagersResponse;
import org.cloudfoundry.client.v2.organizations.ListOrganizationsRequest;
import org.cloudfoundry.client.v2.organizations.ListOrganizationsResponse;
import org.cloudfoundry.client.v2.spaces.CreateSpaceRequest;
import org.cloudfoundry.client.v2.spaces.CreateSpaceResponse;
import org.cloudfoundry.client.v2.spaces.DeleteSpaceRequest;
import org.cloudfoundry.client.v2.spaces.ListSpacesRequest;
import org.cloudfoundry.client.v2.spaces.ListSpacesResponse;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.CreateUserProvidedServiceInstanceRequest;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.CreateUserProvidedServiceInstanceResponse;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.DeleteUserProvidedServiceInstanceRequest;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.ListUserProvidedServiceInstancesRequest;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.ListUserProvidedServiceInstancesResponse;
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
		ListSpacesResponse response = cf.spaces().list(ListSpacesRequest.builder().name(name).organizationId(orgId).build()).get();

		if(response.getResources().size() > 0)
		{
			return Optional.of(response.getResources().get(0).getMetadata().getId());
		}

		return Optional.empty();
	}

	@Override
	public String createSpace(String name, String orgId)
	{
		CreateSpaceResponse response = cf.spaces().create(CreateSpaceRequest.builder().name(name).organizationId(orgId).build()).get();
		return response.getMetadata().getId();
	}

	@Override
	public void deleteSpace(String id)
	{
		cf.spaces().delete(DeleteSpaceRequest.builder().spaceId(id).build()).get();
	}

	@Override
	public Optional<String> findUps(String name, String spaceId)
	{
		ListUserProvidedServiceInstancesResponse response = cf.userProvidedServiceInstances().list(ListUserProvidedServiceInstancesRequest.builder().name(name).build()).get();

		if(response.getResources().size() > 0)
		{
			return Optional.of(response.getResources().get(0).getMetadata().getId());
		}

		return Optional.empty();
	}

	@Override
	public String createUps(String name, Map<String, String> credentials, String spaceId)
	{
		CreateUserProvidedServiceInstanceResponse response = cf.userProvidedServiceInstances().create(CreateUserProvidedServiceInstanceRequest.builder().name(name).credentials(credentials).spaceId(spaceId).build()).get();
		return response.getMetadata().getId();
	}

	@Override
	public void deleteUps(String id)
	{
		cf.userProvidedServiceInstances().delete(DeleteUserProvidedServiceInstanceRequest.builder().userProvidedServiceInstanceId(id).build()).get();
	}

	@Override
	public boolean userExists(String id)
	{
		return false;
	}

	@Override
	public boolean hasOrgRole(String userId, String orgId, OrgRole role)
	{
		if(role == OrgRole.ORG_MANAGER)
		{
			ListOrganizationManagersResponse response = cf.organizations().listManagers(ListOrganizationManagersRequest.builder().organizationId(orgId).build()).get();
			return response.getResources().stream().filter(r -> r.getMetadata().getId().equals(userId)).count() == 1;
		}
		else
		{
			ListOrganizationAuditorsResponse response = cf.organizations().listAuditors(ListOrganizationAuditorsRequest.builder().organizationId(orgId).build()).get();
			return response.getResources().stream().filter(r -> r.getMetadata().getId().equals(userId)).count() == 1;
		}
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
