package kutschke.higherClass;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import com.sun.xml.internal.ws.org.objectweb.asm.Type;

public class ReflectiveFun<ResultType> implements
		GeneralOperation<Object, ResultType> {

	private Method method;

	public ReflectiveFun(String method, Class<?> clazz,
			Class<?>... parameterTypes) throws SecurityException,
			NoSuchMethodException {
		this.method = clazz.getDeclaredMethod(method, parameterTypes);
	}

	@Override
	public ResultType apply(Object... arg) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		Class<?>[] parameterTypes = method.getParameterTypes();
		boolean isStatic = Modifier.isStatic(method.getModifiers());
		Object instance = isStatic ? null : arg[0];
		Object[] args = Arrays.copyOfRange(arg, isStatic ? 0 : 1, arg.length);
		if (method.isVarArgs()) {
			if (args.length > parameterTypes.length
					|| args.length == parameterTypes.length - 1
					|| !args[parameterTypes.length - 1].getClass().equals(
							parameterTypes[parameterTypes.length - 1]))
					 {
				// varargs
				Object[] t_args = Arrays.copyOf(args, parameterTypes.length);
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
		if (isStatic)
			return (ResultType) method.invoke(null, args);
		else
			return (ResultType) method.invoke(instance, args);
	}

}
