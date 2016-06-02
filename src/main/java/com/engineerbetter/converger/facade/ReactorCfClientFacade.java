package com.engineerbetter.converger.facade;

import java.util.List;
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
import org.cloudfoundry.client.v2.organizations.ListOrganizationUsersRequest;
import org.cloudfoundry.client.v2.organizations.ListOrganizationUsersResponse;
import org.cloudfoundry.client.v2.organizations.ListOrganizationsRequest;
import org.cloudfoundry.client.v2.organizations.ListOrganizationsResponse;
import org.cloudfoundry.client.v2.spaces.CreateSpaceRequest;
import org.cloudfoundry.client.v2.spaces.CreateSpaceResponse;
import org.cloudfoundry.client.v2.spaces.DeleteSpaceRequest;
import org.cloudfoundry.client.v2.spaces.ListSpaceAuditorsRequest;
import org.cloudfoundry.client.v2.spaces.ListSpaceAuditorsResponse;
import org.cloudfoundry.client.v2.spaces.ListSpaceDevelopersRequest;
import org.cloudfoundry.client.v2.spaces.ListSpaceDevelopersResponse;
import org.cloudfoundry.client.v2.spaces.ListSpaceManagersRequest;
import org.cloudfoundry.client.v2.spaces.ListSpaceManagersResponse;
import org.cloudfoundry.client.v2.spaces.ListSpacesRequest;
import org.cloudfoundry.client.v2.spaces.ListSpacesResponse;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.CreateUserProvidedServiceInstanceRequest;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.CreateUserProvidedServiceInstanceResponse;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.DeleteUserProvidedServiceInstanceRequest;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.ListUserProvidedServiceInstancesRequest;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.ListUserProvidedServiceInstancesResponse;
import org.cloudfoundry.client.v2.users.ListUsersRequest;
import org.cloudfoundry.client.v2.users.ListUsersResponse;
import org.cloudfoundry.client.v2.users.UserResource;
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
		ListOrganizationsResponse response = cf.organizations().list(ListOrganizationsRequest.builder().name(name).build()).block();
		if(response.getResources().size() > 0)
		{
			return Optional.of(response.getResources().get(0).getMetadata().getId());
		}

		return Optional.empty();
	}

	@Override
	public String createOrg(String name)
	{
		CreateOrganizationResponse response = cf.organizations().create(CreateOrganizationRequest.builder().name(name).build()).block();
		return response.getMetadata().getId();
	}

	@Override
	public void deleteOrg(String id)
	{
		cf.organizations().delete(DeleteOrganizationRequest.builder().organizationId(id).recursive(true).build()).block();
	}

	@Override
	public Optional<String> findSpace(String name, String orgId)
	{
		ListSpacesResponse response = cf.spaces().list(ListSpacesRequest.builder().name(name).organizationId(orgId).build()).block();

		if(response.getResources().size() > 0)
		{
			return Optional.of(response.getResources().get(0).getMetadata().getId());
		}

		return Optional.empty();
	}

	@Override
	public String createSpace(String name, String orgId)
	{
		CreateSpaceResponse response = cf.spaces().create(CreateSpaceRequest.builder().name(name).organizationId(orgId).build()).block();
		return response.getMetadata().getId();
	}

	@Override
	public void deleteSpace(String id)
	{
		cf.spaces().delete(DeleteSpaceRequest.builder().spaceId(id).build()).block();
	}

	@Override
	public Optional<String> findUps(String name, String spaceId)
	{
		ListUserProvidedServiceInstancesResponse response = cf.userProvidedServiceInstances().list(ListUserProvidedServiceInstancesRequest.builder().name(name).build()).block();

		if(response.getResources().size() > 0)
		{
			return Optional.of(response.getResources().get(0).getMetadata().getId());
		}

		return Optional.empty();
	}

	@Override
	public String createUps(String name, Map<String, String> credentials, String spaceId)
	{
		CreateUserProvidedServiceInstanceResponse response = cf.userProvidedServiceInstances().create(CreateUserProvidedServiceInstanceRequest.builder().name(name).credentials(credentials).spaceId(spaceId).build()).block();
		return response.getMetadata().getId();
	}

	@Override
	public void deleteUps(String id)
	{
		cf.userProvidedServiceInstances().delete(DeleteUserProvidedServiceInstanceRequest.builder().userProvidedServiceInstanceId(id).build()).block();
	}

	@Override
	public boolean userExists(String id)
	{
		ListUsersResponse response = cf.users().list(ListUsersRequest.builder().build()).block();
		return response.getResources().stream().filter(u -> u.getMetadata().getId().equals(id)).count() == 1L;
	}

	@Override
	public boolean hasOrgRole(String userId, String orgId, OrgRole role)
	{
		if(role == OrgRole.ORG_MANAGER)
		{
			ListOrganizationManagersResponse response = cf.organizations().listManagers(ListOrganizationManagersRequest.builder().organizationId(orgId).build()).block();
			return response.getResources().stream().filter(r -> r.getMetadata().getId().equals(userId)).count() == 1;
		}
		else
		{
			ListOrganizationAuditorsResponse response = cf.organizations().listAuditors(ListOrganizationAuditorsRequest.builder().organizationId(orgId).build()).block();
			return response.getResources().stream().filter(r -> r.getMetadata().getId().equals(userId)).count() == 1;
		}
	}

	@Override
	public boolean hasSpaceRole(String userId, String spaceId, SpaceRole role)
	{
		List<UserResource> users;
		if(role == SpaceRole.SPACE_AUDITOR)
		{
			ListSpaceAuditorsResponse response = cf.spaces().listAuditors(ListSpaceAuditorsRequest.builder().spaceId(spaceId).build()).block();
			users = response.getResources();
		}
		else if(role == SpaceRole.SPACE_DEVELOPER)
		{
			ListSpaceDevelopersResponse response = cf.spaces().listDevelopers(ListSpaceDevelopersRequest.builder().spaceId(spaceId).build()).block();
			users = response.getResources();
		}
		else
		{
			ListSpaceManagersResponse response = cf.spaces().listManagers(ListSpaceManagersRequest.builder().spaceId(spaceId).build()).block();
			users = response.getResources();
		}

		return users.stream().filter(u -> u.getMetadata().getId().equals(userId)).count() == 1L;
	}

	@Override
	public boolean isUserInOrg(String userId, String orgId)
	{
		ListOrganizationUsersResponse response = cf.organizations().listUsers(ListOrganizationUsersRequest.builder().organizationId(orgId).build()).block();
		return response.getResources().stream().filter(u -> u.getMetadata().getId().equals(userId)).count() == 1L;
	}

}
