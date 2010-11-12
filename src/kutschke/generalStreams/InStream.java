package kutschke.generalStreams;

import java.io.IOException;

/**
 * The general interface for Incoming Streams
 * 
 * @author Michael Kutschke
 * 
 * @param <T>
 */
public interface InStream<T> {
	
	/**
	 * 
	 * @return the next element or null if there are no more
	 */
	public T read() throws IOException;

	/**
	 * fills an array with elements coming from the Stream. Will fill up with
	 * null after all the elements of the Stream are read.
	 * 
	 * @param buf the array to be filled
	 */
	public void read(T[] buf) throws IOException;

	public void read(T[] buf, int offset, int length) throws IOException;

	/**
	 * free any system resources if necessary
	 */
	public void close() throws IOException;
}
