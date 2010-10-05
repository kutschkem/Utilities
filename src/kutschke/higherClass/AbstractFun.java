package kutschke.higherClass;

import java.util.ArrayList;
import java.util.Collection;

/**
 * provides some abstract functions
 * 
 * @author Michael
 * 
 */
public final class AbstractFun {

	protected static final boolean DEBUG_PRINT = true;

	private AbstractFun() {
	}

	/**
	 * filters out all the elements of the collection to which the trait defined
	 * by op applies
	 * 
	 * @param <T>
	 *            the elements in the Collection
	 * @param <S>
	 *            the type of the Collection
	 * @param col
	 *            the Collection to be filtered
	 * @param op
	 *            the function representing the condition on which the elements
	 *            will be transferred into the new Collection
	 * @return normally a collection of the same type as col, an ArrayList if
	 *         the first option is not possible <br/>
	 *         This Collection will contains all elements for which op evaluates
	 *         to true
	 */
	@SuppressWarnings("unchecked")
	public static <T, S extends Collection<T>> Collection<T> filter(S col,
			Lambda<T, Boolean> op) {
		if (col == null)
			return null;
		Collection<T> result = null;
		try {
			result = col.getClass().newInstance();
		} catch (InstantiationException e) {
			result = new ArrayList<T>();
			if (DEBUG_PRINT) {
				System.err
						.println("Couldn't copy the class - instantiate ArrayList instead");
				e.printStackTrace();
			}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (T element : col) {
			if (op.apply(element))
				result.add(element);
		}
		return result;
	}

	/**
	 * applies op to all the elements of col and puts the results together in a
	 * new Collection - preferably of the same type as the original Collection
	 * if possible - otherwise, it returns an ArrayList
	 * 
	 * @param <Arg>
	 *            the elements in the Collection
	 * @param <Res>
	 *            the type of the elements in the new Collection
	 * @param col
	 *            the Collection to be filtered
	 * @param op
	 *            the function that is applied to all of the elements in the
	 *            original Collection
	 * @return normally a collection of the same type as col, an ArrayList if
	 *         the first option is not possible <br/>
	 *         This Collection will contains all elements that were produced
	 *         through op
	 */
	@SuppressWarnings("unchecked")
	public static <Arg, Res, ArgCol extends Collection<Arg>> Collection<Res> map(
			ArgCol col, Lambda<Arg, Res> op) {
		Collection<Res> result = null;
		try {
			result = col.getClass().newInstance();
		} catch (InstantiationException e) {
			result = new ArrayList<Res>();
			if (DEBUG_PRINT) {
				System.err
						.println("Couldn't copy the class - instantiate ArrayList instead");
				e.printStackTrace();
			}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Arg arg : col)
			result.add(op.apply(arg));
		return result;
	}
	

}
