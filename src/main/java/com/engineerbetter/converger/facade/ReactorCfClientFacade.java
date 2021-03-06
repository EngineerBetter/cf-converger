package com.engineerbetter.converger.facade;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v2.organizations.AssociateOrganizationAuditorRequest;
import org.cloudfoundry.client.v2.organizations.AssociateOrganizationManagerRequest;
import org.cloudfoundry.client.v2.organizations.AssociateOrganizationUserRequest;
import org.cloudfoundry.client.v2.organizations.CreateOrganizationRequest;
import org.cloudfoundry.client.v2.organizations.CreateOrganizationResponse;
import org.cloudfoundry.client.v2.organizations.DeleteOrganizationRequest;
import org.cloudfoundry.client.v2.organizations.ListOrganizationAuditorsRequest;
import org.cloudfoundry.client.v2.organizations.ListOrganizationManagersRequest;
import org.cloudfoundry.client.v2.organizations.ListOrganizationUsersRequest;
import org.cloudfoundry.client.v2.organizations.ListOrganizationsRequest;
import org.cloudfoundry.client.v2.organizations.ListOrganizationsResponse;
import org.cloudfoundry.client.v2.spaces.AssociateSpaceAuditorRequest;
import org.cloudfoundry.client.v2.spaces.AssociateSpaceDeveloperRequest;
import org.cloudfoundry.client.v2.spaces.AssociateSpaceManagerRequest;
import org.cloudfoundry.client.v2.spaces.CreateSpaceRequest;
import org.cloudfoundry.client.v2.spaces.CreateSpaceResponse;
import org.cloudfoundry.client.v2.spaces.DeleteSpaceRequest;
import org.cloudfoundry.client.v2.spaces.ListSpaceAuditorsRequest;
import org.cloudfoundry.client.v2.spaces.ListSpaceDevelopersRequest;
import org.cloudfoundry.client.v2.spaces.ListSpaceManagersRequest;
import org.cloudfoundry.client.v2.spaces.ListSpacesRequest;
import org.cloudfoundry.client.v2.spaces.ListSpacesResponse;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.CreateUserProvidedServiceInstanceRequest;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.CreateUserProvidedServiceInstanceResponse;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.DeleteUserProvidedServiceInstanceRequest;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.GetUserProvidedServiceInstanceRequest;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.GetUserProvidedServiceInstanceResponse;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.ListUserProvidedServiceInstancesRequest;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.ListUserProvidedServiceInstancesResponse;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.UpdateUserProvidedServiceInstanceRequest;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.UserProvidedServiceInstanceEntity;
import org.cloudfoundry.client.v2.users.ListUsersRequest;
import org.cloudfoundry.client.v2.users.UserResource;
import org.cloudfoundry.util.PaginationUtils;
import org.cloudfoundry.util.ResourceUtils;
import org.springframework.beans.factory.annotation.Autowired;

import reactor.core.publisher.Flux;

import com.engineerbetter.converger.facade.ops.CreateUserRequest;
import com.engineerbetter.converger.facade.ops.DeleteUserRequest;
import com.engineerbetter.converger.facade.ops.ModifyingUserOps;
import com.engineerbetter.converger.properties.UpsProperties;

public class ReactorCfClientFacade implements CloudFoundryFacade
{
	private final CloudFoundryClient cf;
	private final ModifyingUserOps createUserOps;

	@Autowired
	public ReactorCfClientFacade(CloudFoundryClient cf, ModifyingUserOps createUserOps)
	{
		this.cf = cf;
		this.createUserOps = createUserOps;
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
		ListUserProvidedServiceInstancesResponse response = cf.userProvidedServiceInstances().list(ListUserProvidedServiceInstancesRequest.builder().spaceId(spaceId).name(name).build()).block();

		if(response.getResources().size() > 0)
		{
			return Optional.of(response.getResources().get(0).getMetadata().getId());
		}

		return Optional.empty();
	}


	@Override
	public String createUps(UpsProperties properties, String spaceId)
	{
		CreateUserProvidedServiceInstanceResponse response = cf.userProvidedServiceInstances().create(CreateUserProvidedServiceInstanceRequest.builder().name(properties.name).credentials(properties.credentials).spaceId(spaceId).build()).block();
		return response.getMetadata().getId();
	}


	@Override
	public UpsProperties getUps(String id)
	{
		GetUserProvidedServiceInstanceResponse response = cf.userProvidedServiceInstances().get(GetUserProvidedServiceInstanceRequest.builder().userProvidedServiceInstanceId(id).build()).block();
		UserProvidedServiceInstanceEntity entity = response.getEntity();
		Map<String, Object> entityCredentials = entity.getCredentials();
		return new UpsProperties(entity.getName(), entityCredentials.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString())));
	}


	@Override
	public void updateUps(UpsProperties properties, String spaceId)
	{
		Optional<String> id = findUps(properties.name, spaceId);

		if(!id.isPresent())
		{
			throw new RuntimeException("UPS not found");
		}

		cf.userProvidedServiceInstances().update(UpdateUserProvidedServiceInstanceRequest.builder().credentials(properties.credentials).userProvidedServiceInstanceId(id.get()).build()).block();
	}


	@Override
	public void deleteUps(String id)
	{
		cf.userProvidedServiceInstances().delete(DeleteUserProvidedServiceInstanceRequest.builder().userProvidedServiceInstanceId(id).build()).block();
	}


	@Override
	public boolean userExists(String id)
	{
		Flux<UserResource> allUsers = PaginationUtils.requestClientV2Resources(page -> cf.users().list(ListUsersRequest.builder().page(page).build()));
		return allUsers.any(u -> ResourceUtils.getId(u).equals(id)).block();
	}


	@Override
	public void createUser(String id)
	{
		createUserOps.create(new CreateUserRequest(id)).block();
	}


	@Override
	public void deleteUser(String id)
	{
		createUserOps.delete(new DeleteUserRequest(id, false)).block();
	}


	@Override
	public void addUserToOrg(String userId, String orgId)
	{
		cf.organizations().associateUser(AssociateOrganizationUserRequest.builder().userId(userId).organizationId(orgId).build()).block();
	}


	@Override
	public boolean isUserInOrg(String userId, String orgId)
	{
		Flux<UserResource> users = PaginationUtils.requestClientV2Resources(page -> cf.organizations().listUsers(ListOrganizationUsersRequest.builder().organizationId(orgId).page(page).build()));
		return users.any(u -> ResourceUtils.getId(u).equals(userId)).block();
	}


	@Override
	public boolean hasOrgRole(String userId, String orgId, OrgRole role)
	{
		if(role == OrgRole.MANAGER)
		{
			Flux<UserResource> users = PaginationUtils.requestClientV2Resources(page -> cf.organizations().listManagers(ListOrganizationManagersRequest.builder().organizationId(orgId).build()));
			return users.any(u -> ResourceUtils.getId(u).equals(userId)).block();
		}
		else if(role == OrgRole.AUDITOR)
		{
			Flux<UserResource> users = PaginationUtils.requestClientV2Resources(page -> cf.organizations().listAuditors(ListOrganizationAuditorsRequest.builder().organizationId(orgId).build()));
			return users.any(u -> ResourceUtils.getId(u).equals(userId)).block();
		}
		else
		{
			throw new RuntimeException("Unknown Org Role "+role);
		}
	}


	@Override
	public void setOrgRole(String userId, String orgId, OrgRole role)
	{
		if(role == OrgRole.MANAGER)
		{
			cf.organizations().associateManager(AssociateOrganizationManagerRequest.builder().organizationId(orgId).managerId(userId).build()).block();
		}
		else if(role == OrgRole.AUDITOR)
		{
			cf.organizations().associateAuditor(AssociateOrganizationAuditorRequest.builder().organizationId(orgId).auditorId(userId).build()).block();
		}
		else
		{
			throw new RuntimeException("Unknown Org Role "+role);
		}
	}


	@Override
	public boolean hasSpaceRole(String userId, String spaceId, SpaceRole role)
	{
		Flux<UserResource> users;
		if(role == SpaceRole.AUDITOR)
		{
			users = PaginationUtils.requestClientV2Resources(page -> cf.spaces().listAuditors(ListSpaceAuditorsRequest.builder().spaceId(spaceId).build()));
		}
		else if(role == SpaceRole.DEVELOPER)
		{
			users = PaginationUtils.requestClientV2Resources(page -> cf.spaces().listDevelopers(ListSpaceDevelopersRequest.builder().spaceId(spaceId).build()));

		}
		else if(role == SpaceRole.MANAGER)
		{
			users = PaginationUtils.requestClientV2Resources(page -> cf.spaces().listManagers(ListSpaceManagersRequest.builder().spaceId(spaceId).build()));
		}
		else
		{
			throw new RuntimeException("Unknown Space Role "+role);
		}

		return users.any(u -> ResourceUtils.getId(u).equals(userId)).block();
	}


	@Override
	public void setSpaceRole(String userId, String spaceId, SpaceRole role)
	{
		if(role == SpaceRole.AUDITOR)
		{
			cf.spaces().associateAuditor(AssociateSpaceAuditorRequest.builder().spaceId(spaceId).auditorId(userId).build()).block();
		}
		else if(role == SpaceRole.DEVELOPER)
		{
			cf.spaces().associateDeveloper(AssociateSpaceDeveloperRequest.builder().spaceId(spaceId).developerId(userId).build()).block();
		}
		else if(role == SpaceRole.MANAGER)
		{
			cf.spaces().associateManager(AssociateSpaceManagerRequest.builder().spaceId(spaceId).managerId(userId).build()).block();
		}
		else
		{
			throw new RuntimeException("Unknown Space Role "+role);
		}
	}
}
