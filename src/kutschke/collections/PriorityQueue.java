package kutschke.collections;

public interface PriorityQueue<T> {

	/**
	 * add an element to the Priority Queue
	 * @param t
	 */
	public void add(T t);
	
	/**
	 * signal the priority of an element <i>t</i> has changed. <i>t</i> should
	 * be an element of the Queue, otherwise the behavior is unspecified
	 * @param t
	 */
	public void update(T t);
	
	/**
	 * returns the element with least priority, without removing it
	 * @see #poll()
	 * @return the element with least priority
	 */
	public T peek();
	
	/**
	 * returns and removes the element with least priority
	 * @see #peek()
	 * @return the element with least priority
	 */
	public T poll();
	
	/**
	 * 
	 * @return whether the queue is empty
	 */
	public boolean isEmpty();

	public void clear();
	
}
