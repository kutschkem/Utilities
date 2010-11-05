package kutschke.utility;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ProxyFactory {
	
	@SuppressWarnings("unchecked")
	public static <T> T buildProxy(final Object adaptee, Class<T> interfce, final Object adapter){
		return (T) Proxy.newProxyInstance(interfce.getClassLoader(), new Class[]{interfce}, new InvocationHandler(){
			
			Map<MethodDescriptor,Method> methods = new HashMap<MethodDescriptor,Method>();
			
			{
				for(Method m : adapter.getClass().getDeclaredMethods())
					methods.put(new MethodDescriptor(m), m);
			}
			
			@Override
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				Method m = methods.get(new MethodDescriptor(method));
				if(m != null)
				return m.invoke(adapter, args);
				return method.invoke(adaptee, args);
			}
			
		});
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T buildDelegate(Class<T> target, final String delegateSrc,final Object delegator, final String delegateDest){
		return (T) Proxy.newProxyInstance(target.getClassLoader(), new Class[]{target}, new InvocationHandler(){

			Method dlgt;
			
			{
				for(Method m : delegator.getClass().getMethods())
					if(m.getName().equals(delegateDest))
						dlgt = m;
			}
			
			@Override
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				if(method.getName().equals(delegateSrc))
					dlgt.invoke(delegator, args);
				if(method.getDeclaringClass().equals(Object.class))
					return method.invoke(proxy, args);
				return null;
			}
			
		});
	}
	
	static class MethodDescriptor{
		private final String name;
		private final Class<?>[] params;
		
		public MethodDescriptor(Method m){
			this.name = m.getName();
			this.params = m.getParameterTypes();
		}
		
		@Override
		public boolean equals(Object o){
			if(! (o instanceof MethodDescriptor)) return false;
			MethodDescriptor md = (MethodDescriptor) o;
			return name.equals(md.name) && Arrays.equals(params, md.params);
		}
		
		@Override
		public int hashCode(){
			return name.hashCode() + 31 * params.hashCode();
		}
	}

}
