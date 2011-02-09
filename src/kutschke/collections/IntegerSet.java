package kutschke.collections;

import java.lang.reflect.Array;
import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class IntegerSet implements Set<Integer> {

	BitSet set;
	
	public IntegerSet(){
		set = new BitSet();
	}
	
	public IntegerSet(int capacity){
		set = new BitSet(capacity);
	}

	@Override
	public int size() {
		return set.cardinality();
	}

	@Override
	public boolean isEmpty() {
		return set.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return (o instanceof Integer) && set.get((Integer) o);
	}

	@Override
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>() {

			int last = -1;

			@Override
			public boolean hasNext() {
				return set.nextSetBit(last + 1) != -1;
			}

			@Override
			public Integer next() {
				last = set.nextSetBit(last + 1);
				if (last == -1) {
					last = Integer.MAX_VALUE - 1;
					return null;
				}
				return last;
			}

			@Override
			public void remove() {
				set.clear(last);

			}

		};
	}

	@Override
	public Object[] toArray() {
		Object[] arr = new Object[size()];
		int j = 0;
		for (int i = set.nextSetBit(0); i >= 0; i = set.nextSetBit(i + 1)) {
			arr[j] = i;
			j++;
		}
		return arr;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
		T[] arr = (a.length >= size() ? a : (T[]) Array.newInstance(a
				.getClass().getComponentType(), size()));
		int j = 0;
		for (int i = set.nextSetBit(0); i >= 0; i = set.nextSetBit(i + 1)) {
			Array.set(arr, j, i);
			j++;
		}
		return arr;
	}

	@Override
	public boolean add(Integer e) {
		if (set.get(e))
			return false;
		set.set(e);
		return true;
	}

	@Override
	public boolean remove(Object o) {
		if ((!(o instanceof Integer)) || set.get((Integer) o))
			return false;
		set.clear((Integer) o);
		return true;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object o : c)
			if (!contains(o))
				return false;
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends Integer> c) {
		boolean result = false;
		for (Integer o : c)
			result = add(o) || result; //be aware of shortcut!!
		return result;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean result = false;
		for (Object o : c)
			result = remove(o) || result; //be aware of shortcut!!
		return result;
	}

	@Override
	public void clear() {
		set.clear();

	}

}
