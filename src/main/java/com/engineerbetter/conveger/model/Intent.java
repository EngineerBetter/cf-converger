package com.engineerbetter.conveger.model;

import java.util.Optional;
import java.util.UUID;

import org.cloudfoundry.client.CloudFoundryClient;

public interface Intent
{
	Optional<UUID> resolved();

	void resolve(CloudFoundryClient cfClient);
}
