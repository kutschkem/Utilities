package kutschke.higherClass;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedList;

public class BindableReflectiveFun<ResultType> extends ReflectiveFun<ResultType> {

	private Object bound;
	
	
	public BindableReflectiveFun(String method, Class<?> clazz,
			Class<?>[] parameterTypes) throws SecurityException,
			NoSuchMethodException {
		super(method, clazz, parameterTypes);
	}


	/**
	 * @param bound the bound Object to set
	 */
	public BindableReflectiveFun setBound(Object bound) {
		this.bound = bound;
		return this;
	}


	/**
	 * @return the bound Object
	 */
	public Object getBound() {
		return bound;
	}
	
	@Override
	public ResultType apply(Object... arg) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if(bound != null){
			LinkedList<Object> list = new LinkedList<Object>(Arrays.asList(arg));
			list.addFirst(bound);
			return super.apply(list.toArray());
		}
		return super.apply(arg);
	}

}
