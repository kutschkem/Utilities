package kutschke.generalStreams;

import java.io.IOException;

public abstract class GeneralFilterStream<T> extends GeneralInStream<T> {
	
	private final InStream<T> inStr;
	public GeneralFilterStream(InStream<T> str)
	{
		inStr = str;
	}
	
	@Override
	public final T read() throws IOException {
		T read = null;
		while(!filter(read = inStr.read()));
		return read;
	}
	
	public void close() throws IOException{
		inStr.close();
		super.close();
	}

	
	protected abstract boolean filter(T i);

}
