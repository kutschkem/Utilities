package kutschke.collections;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ArrayMap<T> implements Map<Integer, T> {

    private Object[] vec = new Object[8];
    private int numElements = 0;

    public ArrayMap() {

    }

    public ArrayMap(int initialSize) {
        setSize(initialSize);
    }

    private void setSize(int newSize) {
        vec = Arrays.copyOf(vec, newSize);

    }

    @Override
    public int size() {
        return numElements;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return (key instanceof Integer) && rangeCheck((Integer) key) && vec[(Integer) key] != null;
    }

    private boolean rangeCheck(int key) {
        return key >= 0 && key < vec.length;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value != null)
            for (Object t : vec)
                if (value.equals(t))
                    return true;
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(Object key) {
        if (!containsKey(key))
            return null;
        return (T) vec[(Integer) key];
    }

    @Override
    public T put(Integer key, T value) {
        T former = get(key);
        if (key >= vec.length)
            setSize(2 * key + 1);
        vec[key] = value;
        if (former == null)
            numElements++;
        if (value == null)
            numElements--;
        return former;
    }

    @Override
    public T remove(Object key) {
        T former = get(key);
        vec[(Integer) key] = null;
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
        Arrays.fill(vec, null);
        numElements = 0;
    }

    @Override
    public Set<Integer> keySet() {
        return new AbstractSet<Integer>() {

            @Override
            public Iterator<Integer> iterator() {
                return new Iterator<Integer>() {

                    int index = 0;
                    int vecIndex = -1;

                    @Override
                    public boolean hasNext() {
                        return index < size();
                    }

                    @Override
                    public Integer next() {
                        if (!hasNext())
                            return null;
                        vecIndex++;
                        while (!containsKey(vecIndex)) {
                            vecIndex++;
                        }
                        return vecIndex;
                    }

                    @Override
                    public void remove() {
                        ArrayMap.this.remove(vecIndex);

                    }
                };
            }

            @Override
            public int size() {
                return ArrayMap.this.size();
            }

            @Override
            public boolean contains(Object o) {
                return ArrayMap.this.containsKey(o);
            }

        };

    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<T> values() {
        List<T> values = new LinkedList<T>();
        for (Object t : vec)
            if (t != null)
                values.add((T) t);
        return values;
    }

    @Override
    public Set<java.util.Map.Entry<Integer, T>> entrySet() {
        Set<java.util.Map.Entry<Integer, T>> set = new LinkedHashSet<java.util.Map.Entry<Integer, T>>();
        for (int i = 0; i < vec.length; i++)
            if (vec[i] != null) {
                final Integer j = i;
                set.add(new Entry<Integer, T>() {

                    Integer key = j;
                    @SuppressWarnings("unchecked")
                    T value = (T) vec[j];

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
