package kutschke.collections;

public class PriorityQueueAdapter<T> implements PriorityQueue<T> {
	
	java.util.PriorityQueue<T> pq;
	
	public PriorityQueueAdapter(java.util.PriorityQueue<T> pq){
		this.pq = pq;
	}

	@Override
	public void add(T t) {
		pq.add(t);
	}

	@Override
	public void update(T t) {
		pq.remove(t);
		add(t);

	}

	@Override
	public T peek() {
		return pq.peek();
	}

	@Override
	public T poll() {
		return pq.poll();
	}

	@Override
	public boolean isEmpty() {
		return pq.isEmpty();
	}

	@Override
	public void clear() {
		pq.clear();
		
	}

}
