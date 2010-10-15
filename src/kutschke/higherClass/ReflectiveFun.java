package kutschke.higherClass;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class ReflectiveFun<ResultType> implements
		GeneralOperation<Object, ResultType> {

	protected Method method;

	/**
	 * 
	 * @param method the method name
	 * @param clazz the class in which <i>method</i> is declared
	 * @param parameterTypes the types of the parameters of <i>method</i>. Be very careful
	 * when using multidimensional Object Arrays together with the varargs feature of this class
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public ReflectiveFun(String method, Class<?> clazz,
			Class<?>... parameterTypes) throws SecurityException,
			NoSuchMethodException {
		this.method = clazz.getDeclaredMethod(method, parameterTypes);
	}
 
	/**
	 * Calling a non-static method must be done by puttin the needed instance as first
	 * element of the arguments
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResultType apply(Object... arg) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		Class<?>[] parameterTypes = method.getParameterTypes();
		boolean isStatic = Modifier.isStatic(method.getModifiers());
		// if the method is not static, the first parameter is NOT our instance
		Object instance = isStatic ? null : arg[0];
		Object[] args = Arrays.copyOfRange(arg, isStatic ? 0 : 1, arg.length);
		// varargs
		if (method.isVarArgs()) {
			if (args.length > parameterTypes.length
					|| args.length == parameterTypes.length - 1
					|| !args[parameterTypes.length - 1].getClass().equals(
							parameterTypes[parameterTypes.length - 1]))
					 { // doesn't catch all corner cases, but try best
				Object[] t_args = Arrays.copyOf(args, parameterTypes.length);
				Object[] varargs = Arrays.asList(args).subList(
						t_args.length - 1, args.length).toArray();
				// not enough, we need to make sure the array has the right type
				Object arr = Array.newInstance(
						parameterTypes[parameterTypes.length - 1]
								.getComponentType(), varargs.length);
				for (int i = 0; i < varargs.length; i++) {
					Array.set(arr, i, varargs[i]);
				}
				t_args[t_args.length - 1] = arr;
				args = t_args;
			}

		}
		if (isStatic)
			return (ResultType) method.invoke(null, args);
		else
			return (ResultType) method.invoke(instance, args);
	}

}
