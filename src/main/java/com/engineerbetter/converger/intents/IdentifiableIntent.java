package com.engineerbetter.converger.intents;

import java.util.Optional;

public interface IdentifiableIntent extends Intent
{
	Optional<String> id();
}
