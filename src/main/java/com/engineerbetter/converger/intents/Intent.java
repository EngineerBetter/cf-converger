package com.engineerbetter.converger.intents;

import com.engineerbetter.converger.resolution.Resolution;

public interface Intent<R extends Resolution>
{
	R getResolution();

	void setResolution(R resolution);
}
