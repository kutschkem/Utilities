package kutschke.utility;

/**
 * simple pair class
 * @author Michael
 *
 * @param <T1>
 * @param <T2>
 */
public class Pair<T1, T2> {
	public T1 o1;
	public T2 o2;

	public Pair(T1 o1, T2 o2) {
		this.o1 = o1;
		this.o2 = o2;
	}

	public T1 getFirst() {
		return o1;
	}

	public T2 getSecond() {
		return o2;
	}

	public void setFirst(T1 o) {
		o1 = o;
	}

	public void setSecond(T2 o) {
		o2 = o;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Pair))
			return false;
		Pair p = (Pair) obj;
		return same(p.o1, this.o1) && same(p.o2, this.o2);
	}

	private boolean same(Object o1, Object o2) {
		return o1 == null ? o2 == null : o1.equals(o2);
	}

	@Override
	public String toString() {
		return "Pair{" + o1 + ", " + o2 + "}";
	}

	@Override
	public int hashCode() {
		return o1.hashCode() * o2.hashCode();
	}

}
