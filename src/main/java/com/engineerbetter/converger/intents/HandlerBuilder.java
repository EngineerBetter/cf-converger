package com.engineerbetter.converger.intents;

import com.engineerbetter.converger.resolution.Resolution;

public interface HandlerBuilder<I extends Intent<? extends Resolution>>
{
	Handler<I> build(I intent);
}
