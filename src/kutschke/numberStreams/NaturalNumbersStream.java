package kutschke.numberStreams;

import java.util.Iterator;

import kutschke.generalStreams.GeneralInStream;
import kutschke.generalStreams.iterators.StreamIterator;

public class NaturalNumbersStream extends GeneralInStream<Integer> implements Iterable<Integer>{

	private int state = 0;
	
	public NaturalNumbersStream()
	{
		
	}
	
	public NaturalNumbersStream(int start)
	{
		state = start;
	}
	
	@Override
	public Integer read() {
		state++;
		return state;
	}

	@Override
	public Iterator<Integer> iterator() {

				return new StreamIterator<Integer>(NaturalNumbersStream.this);
	}

}
