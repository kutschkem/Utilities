package kutschke.interfaces;

public interface Optimizable<T> {

	/**
	 * optimizes the internals of this Object. Afterwards, the internal state
	 * should not be altered from the outside. Also, it is not garantueed that the
	 * returned, optimized Object is the same as this Object.
	 * @return
	 */
	public T optimize();
	
}
