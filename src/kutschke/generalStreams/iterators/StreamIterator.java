package kutschke.generalStreams.iterators;

import java.io.IOException;
import java.util.Iterator;

import kutschke.exceptions.ExceptionWrapper;
import kutschke.generalStreams.InStream;

public class StreamIterator<E> implements Iterator<E> {

	protected final InStream<E> inStr;
	protected boolean checked = false;
	protected E buffer = null;

	public StreamIterator(InStream<E> str) {
		inStr = str;
	}

	@Override
	public boolean hasNext() {
		try {
			buffer = inStr.read();
			checked = true;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * @throws ExceptionWrapper
	 *             contains the IOException thrown by the Stream - be sure to
	 *             catch this if you actually use any streams that could throw
	 *             IOExceptions
	 */
	@Override
	public E next() {
		if (!checked)
			try {
				return inStr.read();
			} catch (IOException e) {
				throw new ExceptionWrapper(e);
			}
		else {
			checked = false;
			return buffer;
		}
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("No remove Operation supported");

	}

}
