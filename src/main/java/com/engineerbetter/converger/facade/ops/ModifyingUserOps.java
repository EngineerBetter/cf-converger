package com.engineerbetter.converger.facade.ops;

import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.v2.AbstractClientV2Operations;

import reactor.core.publisher.Mono;

/**
 * Horrible hackery whilst we wait for this functionality to arrive in cf-java-client
 *
 * @see CreateUserRequest
 *
 */
public class ModifyingUserOps extends AbstractClientV2Operations
{
	public ModifyingUserOps(ConnectionContext connectionContext, Mono<String> root, TokenProvider tokenProvider) {
		super(connectionContext, root, tokenProvider);
	}

	public Mono<Void> create(CreateUserRequest request)
	{
		return post(request, Void.class, builder -> builder.pathSegment("v2", "users"));
	}


	public Mono<Void> delete(DeleteUserRequest request)
	{
		return delete(request, Void.class, builder -> builder.pathSegment("v2", "users", request.guid));
	}
}