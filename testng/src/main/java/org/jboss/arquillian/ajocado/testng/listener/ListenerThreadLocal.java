package org.jboss.arquillian.ajocado.testng.listener;

import java.util.HashMap;
import java.util.Map;

public class ListenerThreadLocal<T> {

	private T initialValue;

	private Map<Class<? extends AbstractConfigurationListener>, ThreadLocal<T>> map = new HashMap<Class<? extends AbstractConfigurationListener>, ThreadLocal<T>>() {
		public java.lang.ThreadLocal<T> get(Object key) {
			if (key instanceof Class
					&& ((Class<?>) key)
							.isAssignableFrom(AbstractConfigurationListener.class)) {
				if (!containsKey(key)) {
					put((Class<? extends AbstractConfigurationListener>) key,
							new ThreadLocal<T>() {
								protected T initialValue() {
									return initialValue;
								};
							});
				}
				return get(key);
			}
			return null;
		}
	};

	public ListenerThreadLocal(T initialValue) {
		this.initialValue = initialValue;
	}

	public synchronized T get(Class<? extends AbstractConfigurationListener> listenerType) {
		return map.get(listenerType).get();
	}

	public synchronized void set(Class<? extends AbstractConfigurationListener> listenerType, T value) {
		ThreadLocal<T> threadLocal = map.get(listenerType);
		threadLocal.get();
	}
}
