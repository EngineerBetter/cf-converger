package com.engineerbetter.converger.facade;

import java.util.Optional;

import com.engineerbetter.converger.properties.UpsProperties;

public interface CloudFoundryFacade
{
	public enum OrgRole
	{
		MANAGER,
		AUDITOR
	}

	public enum SpaceRole
	{
		MANAGER,
		DEVELOPER,
		AUDITOR
	}

	Optional<String> findOrg(String name);
	String createOrg(String name);
	void deleteOrg(String id);


	Optional<String> findSpace(String name, String orgId);
	String createSpace(String name, String orgId);
	void deleteSpace(String id);

	Optional<String> findUps(String name, String spaceId);
	String createUps(UpsProperties properties, String spaceId);
	UpsProperties getUps(String id);
	void updateUps(UpsProperties properties, String spaceId);
	void deleteUps(String id);

	boolean userExists(String id);
	void createUser(String id);
	void deleteUser(String id);
	boolean isUserInOrg(String userId, String orgId);
	void addUserToOrg(String userId, String orgId);
	boolean hasOrgRole(String userId, String orgId, OrgRole role);
	void setOrgRole(String userId, String orgId, OrgRole role);
	boolean hasSpaceRole(String userId, String spaceId, SpaceRole role);
}
