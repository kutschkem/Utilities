package kutschke.collections;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class VectorMap<T> implements Map<Integer, T> {

	Vector<T> vec = new Vector<T>();
	int numElements = 0;

	@Override
	public int size() {
		return numElements;
	}

	@Override
	public boolean isEmpty() {
		return vec.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		try {
			return (key instanceof Integer) && vec.get((Integer) key) != null;
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

	@Override
	public boolean containsValue(Object value) {
		if (value != null)
			for (T t : vec)
				if (value.equals(t))
					return true;
		return false;
	}

	@Override
	public T get(Object key) {
		if (!(key instanceof Integer))
			throw new IllegalArgumentException();
		if (!containsKey(key))
			return null;
		return vec.get((Integer) key);
	}

	@Override
	public T put(Integer key, T value) {
		T former = get(key);
		if (key >= vec.size())
			vec.setSize(key + 1);
		vec.setElementAt(value, key);
		if (former == null)
			numElements++;
		if (value == null)
			numElements--;
		return former;
	}

	@Override
	public T remove(Object key) {
		T former = get(key);
		vec.setElementAt(null, (Integer) key);
		if (former != null)
			numElements--;
		return former;
	}

	@Override
	public void putAll(Map<? extends Integer, ? extends T> m) {
		for (Integer i : m.keySet())
			put(i, m.get(i));
	}

	@Override
	public void clear() {
		vec.clear();
		numElements = 0;
	}

	@Override
	public Set<Integer> keySet() {
		Set<Integer> set = new IntegerSet();
		for (int i = 0; i < vec.size(); i++)
			if (vec.get(i) != null)
				set.add(i);
		return set;
	}

	@Override
	public Collection<T> values() {
		List<T> values = new LinkedList<T>();
		for (T t : vec)
			if (t != null)
				values.add(t);
		return values;
	}

	@Override
	public Set<java.util.Map.Entry<Integer, T>> entrySet() {
		Set<java.util.Map.Entry<Integer, T>> set = new LinkedHashSet<java.util.Map.Entry<Integer, T>>();
		for (int i = 0; i < vec.size(); i++)
			if (vec.get(i) != null) {
				final Integer j = i;
				set.add(new Entry<Integer, T>() {

					Integer key = j;
					T value = vec.get(j);

					@Override
					public Integer getKey() {
						return key;
					}

					@Override
					public T getValue() {
						return value;
					}

					@Override
					public T setValue(T value) {
						this.value = value;
						return value;
					}

				});
			}
		return set;
	}

}
