package kutschke.higherClass;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class ReflectiveFun<ResultType> implements
		GeneralOperation<Object, ResultType> {

	private Class<?> instanceClass;
	private Method method;

	public ReflectiveFun(String method, Class<?> clazz,
			Class<?>... parameterTypes) throws SecurityException,
			NoSuchMethodException {
		this.method = clazz.getDeclaredMethod(method, parameterTypes);
		this.instanceClass = clazz;
	}

	@Override
	public ResultType apply(Object... arg) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		Class<?>[] parameterTypes = method.getParameterTypes();
		Object instance = arg[0];
		Object[] args = Arrays.copyOfRange(arg, 1, arg.length);
		if (args.length >= parameterTypes.length && args.length > 0)
			if (parameterTypes[parameterTypes.length - 1].isArray()) {
				if (args.length > parameterTypes.length
						|| !args[parameterTypes.length - 1].getClass().equals(
								parameterTypes[parameterTypes.length - 1])) {
					// varargs
					Object[] t_args = Arrays
							.copyOf(args, parameterTypes.length);
					Object[] varargs = Arrays.asList(args).subList(
							t_args.length - 1, args.length).toArray();
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
		if (Modifier.isStatic(method.getModifiers()))
			method.invoke(null, arg);
		else
			method.invoke(instance, args);
		return null;
	}

}
