package com.engineerbetter.converger.intents;

import java.util.List;

public interface IntentResolver
{
	void resolve(List<Intent> orderedIntents);
}
