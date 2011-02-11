package kutschke.utility.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ProxyFactory {

	/**
	 * builds a proxy that relays all methods that exist in the adapter to the
	 * adapter, the rest remains dispatched by the adaptee. Note that for
	 * different reasons, no methods are relayed that are not declared in the
	 * adapter's class (therefore, no inherited methods). <br/>
	 * E.g. this results in equal hashcodes of the Proxy and the adaptee if the
	 * adapter does not override hashcode()
	 * 
	 * @param <T>
	 * @param adaptee
	 * @param interfce
	 * @param adapter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T buildProxy(final Object adaptee, Class<T> interfce,
			final Object adapter) {
		return (T) Proxy.newProxyInstance(interfce.getClassLoader(),
				new Class[] { interfce }, new InvocationHandler() {

					Map<MethodDescriptor, Method> methods = new HashMap<MethodDescriptor, Method>();

					{
						for (Method m : adapter.getClass().getDeclaredMethods())
							methods.put(new MethodDescriptor(m), m);
					}

					@Override
					public Object invoke(Object proxy, Method method,
							Object[] args) throws Throwable {
						Method m = methods.get(new MethodDescriptor(method));
						if (m != null)
							return m.invoke(adapter, args);
						return method.invoke(adaptee, args);
					}

				});
	}

	/**
	 * build a delegator proxy for one specific method in the target class.
	 * 
	 * @param <T>
	 * @param target the target class
	 * @param delegateSrc the method in the target class that gets mapped to delegateDest
	 * @param delegator
	 * @param delegateDest the method name of the method that actually gets called in the delegator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T buildDelegate(Class<T> target,
			final String delegateSrc, final Object delegator,
			final String delegateDest) {
		return (T) Proxy.newProxyInstance(target.getClassLoader(),
				new Class[] { target }, new InvocationHandler() {

					Method dlgt;

					{
						for (Method m : delegator.getClass().getMethods())
							if (m.getName().equals(delegateDest))
								dlgt = m;
					}

					@Override
					public Object invoke(Object proxy, Method method,
							Object[] args) throws Throwable {
						if (method.getName().equals(delegateSrc))
							dlgt.invoke(delegator, args);
						if (method.getDeclaringClass().equals(Object.class))
							return method.invoke(proxy, args);
						return null;
					}

				});
	}

	/**
	 * for a known class, the method descriptor uniquely identifies a method of
	 * that class
	 * 
	 * @author Michael
	 * 
	 */
	public static class MethodDescriptor {
		private final String name;
		private final Class<?>[] params;

		public MethodDescriptor(Method m) {
			this.name = m.getName();
			this.params = m.getParameterTypes();
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof MethodDescriptor))
				return false;
			MethodDescriptor md = (MethodDescriptor) o;
			return name.equals(md.name) && Arrays.equals(params, md.params);
		}

		@Override
		public int hashCode() {
			return name.hashCode() + 31 * params.hashCode();
		}
	}

}
