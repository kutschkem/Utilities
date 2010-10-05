package kutschke.numberStreams;

import kutschke.generalStreams.InStream;

public class OddNumFilterStream extends NumberFilterStream {

	public OddNumFilterStream(InStream<Integer> str)
	{
		super(str);
	}

	@Override
	public boolean filter(Integer i){
		return (i % 2) == 1;
	}
}
