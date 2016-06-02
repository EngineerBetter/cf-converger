package com.engineerbetter.converger.intents;

import java.util.List;

import com.engineerbetter.converger.model.Declaration;
import com.engineerbetter.converger.resolution.Resolution;

public interface OrderedHandlerBuilder
{
	List<Handler<? extends Intent<? extends Resolution>>> getOrderedHandlers(Declaration declaration);
}