package com.engineerbetter.converger.facade;

import java.util.Map;
import java.util.Optional;

public interface CloudFoundryFacade
{
	public enum OrgRole
	{
		ORG_MANAGER,
		ORG_AUDITOR
	}

	public enum SpaceRole
	{
		SPACE_MANAGER,
		SPACE_DEVELOPER,
		SPACE_AUDITOR
	}

	Optional<String> findOrg(String name);
	String createOrg(String name);
	void deleteOrg(String id);


	Optional<String> findSpace(String name, String orgId);
	String createSpace(String name, String orgId);
	void deleteSpace(String id);

	Optional<String> findUps(String name, String spaceId);
	String createUps(String name, Map<String, String> credentials, String spaceId);
	void deleteUps(String id);

	boolean userExists(String id);
	boolean hasOrgRole(String userId, String orgId, OrgRole role);
	boolean hasSpaceRole(String userId, String spaceId, SpaceRole role);
	boolean isUserInOrg(String userId, String orgId);
}
