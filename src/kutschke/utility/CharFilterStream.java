package kutschke.utility;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * use this to filter specific chars out of a stream. This means they won't be in it
 * afterwards. Especially usefull to filter out null characters.
 * @author Michael
 *
 */
public class CharFilterStream extends FilterOutputStream {

	private int filter = 0;
	
	public CharFilterStream(OutputStream arg0){
		super(arg0);
	}
	
	public CharFilterStream(char filtered, OutputStream arg0) {
		super(arg0);
		filter = filtered;
	}

	@Override
	public void write(int b) throws IOException {
		if(b != filter)
			out.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException{
		for(int i = off; i < len ; i++)
			write(b[i]);
	}

	@Override
	public void write(byte[] b) throws IOException{
		write(b, 0, b.length);
	}
	
}
