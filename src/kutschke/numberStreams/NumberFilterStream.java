package kutschke.numberStreams;

import java.util.Iterator;

import kutschke.generalStreams.GeneralFilterStream;
import kutschke.generalStreams.InStream;
import kutschke.generalStreams.iterators.StreamIterator;

public abstract class NumberFilterStream extends GeneralFilterStream<Integer> implements Iterable<Integer> {

	public NumberFilterStream(InStream<Integer> str) {
		super(str);
	}

	/**
	 * this is for the lazy - be careful how you use it since you could easily
	 * generate a never-ending loop by iterating over the elements coming from a
	 * never-ending stream. Consider using {@link SecureNumberStreamIterator}
	 */
	@Override
	public Iterator<Integer> iterator() {
				return new StreamIterator<Integer>(NumberFilterStream.this);
			
	}
}
