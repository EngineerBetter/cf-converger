package com.engineerbetter.converger.intents;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.engineerbetter.converger.resolution.Resolution;
import com.fasterxml.jackson.core.type.TypeReference;

public class TypeReferenceMapHandlerFactory implements HandlerFactory
{
	private Map<Type, Object> handlers = new HashMap<>();

	public <T extends HandlerBuilder<? extends Intent<? extends Resolution>>> void put(TypeReference<T> ref, T thing)
	{
		ParameterizedType type = (ParameterizedType) ref.getType();
		Type[] actualTypeArguments = type.getActualTypeArguments();
		handlers.put(actualTypeArguments[0], thing);
	}


	@Override
	public <I extends Intent<? extends Resolution>> Handler<I> build(I intent)
	{
		@SuppressWarnings("unchecked")
		HandlerBuilder<I> builder = (HandlerBuilder<I>) handlers.get(intent.getClass());
		return builder.build(intent);
	}
}
