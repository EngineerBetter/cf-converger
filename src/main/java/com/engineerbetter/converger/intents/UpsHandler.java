package com.engineerbetter.converger.intents;

public abstract class UpsHandler extends Handler<UpsIntent>
{
	public UpsHandler(UpsIntent intent)
	{
		super(intent);
	}
}
