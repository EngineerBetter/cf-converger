package com.engineerbetter.converger.resolution;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class MutableResolutionTest
{
	@Test
	public void absent()
	{
		MutableResolution resolution = MutableResolution.absent();
		assertThat(resolution.exists(), is(false));
		assertThat(resolution.getDifference().isPresent(), is(false));
	}


	@Test
	public void same()
	{
		MutableResolution resolution = MutableResolution.same("id");
		assertThat(resolution.exists(), is(true));
		assertThat(resolution.getId().get(), equalTo("id"));
		assertThat(resolution.getDifference().isPresent(), is(false));
	}
}
