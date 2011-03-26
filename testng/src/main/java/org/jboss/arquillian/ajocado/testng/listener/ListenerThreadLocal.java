package org.jboss.arquillian.ajocado.testng.listener;

import java.util.HashMap;
import java.util.Map;

public class ListenerThreadLocal<T> {

	private Map<Class<? extends AbstractConfigurationListener>, ThreadLocal<T>> map = new HashMap<Class<? extends AbstractConfigurationListener>, ThreadLocal<T>>() {
		@Override
		public java.lang.ThreadLocal<T> get(Object key) {
			if (key instanceof Class
					&& AbstractConfigurationListener.class.isAssignableFrom((Class<?>) key)) {
				if (!containsKey(key)) {
					put((Class<? extends AbstractConfigurationListener>) key,
							new ThreadLocal<T>() {
								protected T initialValue() {
									return ListenerThreadLocal.this
											.initialValue();
								};
							});
				}
				return super.get(key);
			}
			return null;
		}
	};

	protected T initialValue() {
		return null;
	}

	public synchronized T get(
			Class<? extends AbstractConfigurationListener> listenerType) {
		return map.get(listenerType).get();
	}

	public synchronized void set(
			Class<? extends AbstractConfigurationListener> listenerType, T value) {
		ThreadLocal<T> threadLocal = map.get(listenerType);
		threadLocal.set(value);
	}
}
