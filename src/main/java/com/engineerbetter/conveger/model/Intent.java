package com.engineerbetter.conveger.model;

import java.util.Optional;
import java.util.UUID;

public interface Intent
{
	Optional<UUID> resolved();

	void resolve();
}
