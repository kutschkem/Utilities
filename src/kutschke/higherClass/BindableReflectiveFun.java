package kutschke.higherClass;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedList;

public class BindableReflectiveFun<ResultType> extends
		ReflectiveFun<ResultType> {

	private Object bound;

	/**
	 * @see ReflectiveFun#ReflectiveFun(String, Class, Class[])
	 * @param method
	 * @param clazz
	 * @param parameterTypes
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public BindableReflectiveFun(String method, Class<?> clazz,
			Class<?>[] parameterTypes) throws SecurityException,
			NoSuchMethodException {
		super(method, clazz, parameterTypes);
	}

	/**
	 * @param bound
	 *            the bound Object to set
	 * @throws ClassCastException
	 *             if bound is not Compatible to the represented method
	 */
	public BindableReflectiveFun<ResultType> setBound(Object bound) {
		this.bound = bound;
		if (method.getDeclaringClass().isInstance(bound))
			throw new ClassCastException(bound.getClass()
					+ " is not compatible with " + method.getDeclaringClass());
		return this;
	}

	/**
	 * @return the bound Object
	 */
	public Object getBound() {
		return bound;
	}

	/**
	 * if bound is set, apply the method to it, otherwise, if it is static,
	 * execute the method. If the method is non-static, try applying it to the
	 * first argument
	 */
	@Override
	public ResultType apply(Object... arg) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		if (bound != null) {
			LinkedList<Object> list = new LinkedList<Object>(Arrays.asList(arg));
			list.addFirst(bound);
			return super.apply(list.toArray());
		}
		return super.apply(arg);
	}

}
