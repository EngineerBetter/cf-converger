package com.engineerbetter.converger.intents;

import com.engineerbetter.converger.resolution.Resolution;


public abstract class Handler<I extends Intent<? extends Resolution>>
{
	protected I intent;

	public Handler(I intent)
	{
		this.intent = intent;
	}

	public abstract void resolve();

	public abstract void converge();

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((intent == null) ? 0 : intent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		Handler<I> other = (Handler<I>) obj;
		if (intent == null)
		{
			if (other.intent != null)
				return false;
		} else if (!intent.equals(other.intent))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return this.getClass().getSimpleName()+" : " + intent;
	}
}
