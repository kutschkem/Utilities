package kutschke.higherClass;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.Arrays;

public class ConstructorFun<ResultType> implements
		GeneralOperation<Object, ResultType> {

	private Constructor<ResultType> cons;

	public ConstructorFun(Class<ResultType> clazz, Class<?>... parameterTypes)
			throws SecurityException, NoSuchMethodException {
		cons = clazz.getConstructor(parameterTypes);
	}

	@Override
	public ResultType apply(Object... arg) throws Exception {
		Class<?>[] parameterTypes = cons.getParameterTypes();
		// varargs
		if (cons.isVarArgs()) {
			if (arg.length > parameterTypes.length
					|| arg.length == parameterTypes.length - 1
					|| !arg[parameterTypes.length - 1].getClass().equals(
							parameterTypes[parameterTypes.length - 1])) {
				/*
				 * doesn't catch all corner cases, but try
				 * best
				 */
				Object[] t_args = Arrays.copyOf(arg, parameterTypes.length);
				Object[] varargs = Arrays.asList(arg)
						.subList(t_args.length - 1, arg.length).toArray();
				// not enough, we need to make sure the array has the right type
				Object arr = Array.newInstance(
						parameterTypes[parameterTypes.length - 1]
								.getComponentType(), varargs.length);
				for (int i = 0; i < varargs.length; i++) {
					Array.set(arr, i, varargs[i]);
				}
				t_args[t_args.length - 1] = arr;
				arg = t_args;
			}

		}
		return (ResultType) cons.newInstance(arg);
	}

	@Override
	public String toString() {
		return cons.toGenericString();
	}

}
