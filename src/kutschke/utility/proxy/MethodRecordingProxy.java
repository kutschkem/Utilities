package kutschke.utility.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import kutschke.higherClass.Lambda;

/**
 * records method calls done through the proxy instances, allows later replaying of the
 * recorded sequences of method calls.
 * @author Michael
 *
 * @param <T>
 */
public class MethodRecordingProxy<T> {

	private T proxy;
	List<Lambda<?, ?, ?>> records = new ArrayList<Lambda<?, ?, ?>>();
	boolean recording = false;

	public MethodRecordingProxy(T proxy) {
		this.proxy = proxy;
	}

	public void clearRecords() {
		records.clear();
	}

	public void startRecording() {
		recording = true;
	}

	public void stopRecording() {
		recording = false;
	}

	@SuppressWarnings("unchecked")
	public T proxy() {
		return (T) Proxy.newProxyInstance(proxy.getClass().getClassLoader(),
				new Class[] { proxy.getClass() }, new InvocationHandler() {

					@Override
					public Object invoke(Object proxy,
							final Method method, final Object[] args)
							throws Throwable {
						if (recording)
							records.add(new Lambda<Void, Object, Exception>() {

								@Override
								public Object apply(Void arg)
										throws Exception {
									return method.invoke(MethodRecordingProxy.this.proxy, args);
								}
							});
						return method.invoke(MethodRecordingProxy.this.proxy, args);
					}

				});
	}

	/**
	 * replay the method call sequence
	 * @return the last returned value
	 * @throws Exception if any of the underlying methods throw one
	 */
	public Object replay() throws Exception {
		Object result = null;
		for (Lambda<?, ?, ?> lambda : records) {
			result = lambda.apply(null);
		}
		return result;
	}

}
