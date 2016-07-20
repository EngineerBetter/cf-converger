package com.engineerbetter.converger.resolution;

import com.engineerbetter.converger.intents.Intent;


public interface Resolution
{
	boolean exists();

	String convergenceDescription(Intent<? extends Resolution> intent);
}
