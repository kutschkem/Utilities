package kutschke.generalStreams.iterators;

import java.util.NoSuchElementException;

import kutschke.generalStreams.InStream;
import kutschke.higherClass.Lambda;
import kutschke.higherClass.NoThrowLambda;

/**
 * This class provides a more secure Iteration of the Stream because of a
 * hopefully well defined Break-Condition which is provided by your
 * implementation. This way there will be no endless loop (hopefully).
 * 
 * @author Michael Kutschke
 */
public abstract class SecuredStreamIterator<E> extends StreamIterator<E> {

	/**
	 * Constructs an iterator over a stream. Note that it doesn't make much sense
	 * to have two at a time, because it will result in a not very predictable
	 * behavior.
	 * 
	 * @param str
	 */
	public SecuredStreamIterator(InStream<E> str) {
		super(str);
	}

	/**
	 * @return true as long as the stream has elements left and the break
	 * condition is not violated
	 */
	@Override
	public boolean hasNext() {
		if (!super.hasNext())
			return false;
		return !breakCondition(buffer);
	}

	/**
	 * gets the next element
	 * @throws NoSuchElementException if there are no elements left - you should always check with hasNext() first
	 * @see {@link hasNext}
	 */
	@Override
	public E next() {
		E buf = super.next();
		if (breakCondition(buf))
			throw new NoSuchElementException();
		return buf;
	}

	/**
	 * the Iterator will stop once this returns true
	 * @param e
	 * @return true if the iterator should stop, <br/> false if everything is okay
	 */
	protected abstract Boolean breakCondition(E e);

	public static <T> SecuredStreamIterator<T> getSecureIterator(
			InStream<T> inStr, NoThrowLambda<T, Boolean> breakCond) {
		final NoThrowLambda<T, Boolean> bC = breakCond;
		return new SecuredStreamIterator<T>(inStr) {

			@Override
			protected Boolean breakCondition(T e) {

				return bC.apply(e);
			}

		};
	}
}
