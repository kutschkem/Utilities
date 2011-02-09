package kutschke.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections15.list.TreeList;

import kutschke.higherClass.NoThrowLambda;

/**
 * An Implementation of Dial's Datastructure. Uses a value function <i>f</i> to
 * compute the Priority of an element. For all elements e added or updated it
 * must be satisfied that f(peek()) <= f(e) + maxValueDiff.
 * There are no checks for sake of performance, so this contract needs to be
 * enforced by the developer.
 * 
 * @author Michael
 * 
 */
public class DialQueue<T> implements PriorityQueue<T> {

	ArrayList<T>[] buckets;
	int currentValue = 0;
	/**
	 * Maps an element to its Priority Value.
	 */
	NoThrowLambda<T, Integer> valueFkt;
	/**
	 * saves an element's bucket
	 */
	Map<T, Integer> reverseMapping = new HashMap<T, Integer>();

	@SuppressWarnings("unchecked")
	public DialQueue(int startValue, int maxValueDiff,
			NoThrowLambda<T, Integer> valueFkt) {
		buckets = new ArrayList[maxValueDiff + 1]; // one more b/c we start
													// counting from zero
		for (int i = 0; i < buckets.length; i++) {
			buckets[i] = new ArrayList<T>();
		}
		this.valueFkt = valueFkt;
		currentValue = startValue % buckets.length;
	}

	@Override
	public void add(T t) {
		int bucket = valueFkt.apply(t) % buckets.length;
		if (bucket < 0)
			bucket += buckets.length;
		buckets[bucket].add(t);
		reverseMapping.put(t, bucket);
	}

	@Override
	public void update(T t) {
		buckets[reverseMapping.get(t)].remove(t);
		add(t);

	}
	
	/**
	 * returns the first element in the queue without removing it. Null if the
	 * queue is empty
	 */
	@Override
	public T peek() {
		Iterator<T> it = iterateToNext();
		if (it.hasNext())
			return it.next();
		else
			return null;
	}

	private Iterator<T> iterateToNext() {
		int start = currentValue;
		while (buckets[currentValue].isEmpty()) {
			currentValue = (currentValue + 1) % buckets.length;
			if (start == currentValue)
				break;
		}
		Iterator<T> it = buckets[currentValue].iterator();
		return it;
	}

	@Override
	public T poll() {
		Iterator<T> it = iterateToNext();
		T next = it.next();
		it.remove();
		reverseMapping.remove(next); //avoid large hash table
		return next;
	}

	@Override
	public boolean isEmpty() {
		return peek() == null;
	}
	
	@Override
	public void clear(){
		for(List<T> lst: buckets)
			lst.clear();
	}

}
