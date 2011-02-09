package kutschke.collections;

import edu.uci.ics.jung.algorithms.util.MapBinaryHeap;

/**
 * adapts a MapBinaryHeap to the PriorityQueue Interface
 * @author Michael
 *
 * @param <T> the type of the elements in the queue
 */
public class MapBinaryHeapAdapter<T> implements PriorityQueue<T> {

	MapBinaryHeap<T> heap;
	
	public MapBinaryHeapAdapter(MapBinaryHeap<T> heap){
		this.heap = heap;
	}
	
	@Override
	public void add(T t) {
		heap.add(t);

	}

	@Override
	public void update(T t) {
		heap.update(t);
	}

	@Override
	public T peek() {
		return heap.peek();
	}

	@Override
	public T poll() {
		return heap.poll();
	}

	@Override
	public boolean isEmpty() {
		return heap.isEmpty();
	}

	@Override
	public void clear() {
		heap.clear();
		
	}

}
