package kutschke.generalStreams;

import java.io.IOException;

/**
 * just as the Interface {@link InStream}, but it implements methods that
 * shouldn't be changed in other extending classes
 * 
 * @author Michael
 * 
 * @param <T>
 *            the type of the elements of the stream
 */
public abstract class GeneralInStream<T> implements InStream<T> {
	@Override
	public void read(T[] buf) throws IOException {
		read(buf, 0, buf.length);
	}

	@Override
	public void read(T[] buf, int offs, int len) throws IOException {
		for (int i = offs; i < offs + len; i++) {
				buf[i] = read();
		}
	}

	@Override
	public void close() throws IOException {

	}

}
