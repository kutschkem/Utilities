package kutschke.utility;

/**
 * a mutable wrapper for Immutable Objects. Can be used as return channel, for example
 * @author Michael
 *
 * @param <T>
 */
public class Wrapper<T> {

	private T o;
	
	public Wrapper(T o){
		this.o = o;
	}
	
	public Wrapper<T> set(T o){
		this.o = o;
		return this;
	}
	
	public T get(){
		return o;
	}
	
}
