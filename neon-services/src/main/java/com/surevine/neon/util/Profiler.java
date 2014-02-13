package com.surevine.neon.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.google.common.base.Joiner;

public class Profiler<T> {

	@SuppressWarnings("unchecked")
	public T wrap(final Class<T> contract, final T instance) {
		return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] { contract }, new InvocationHandler() {
			// @Override
			public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
				final long start = System.currentTimeMillis();

				try {
					return method.invoke(instance, args);
				} finally {
					final long duration = System.currentTimeMillis() - start;

					if (duration > 0) {
						System.out.println(String.format("Executed %s( %s ) in %dms.", method.getName(), Joiner.on(", ").join(args), duration));
					}
				}
			}
		});
	}
}