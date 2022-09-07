package kutschke.utility;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Runtime Exception Wrapper for "true" Exceptions. Will only print it's cause's
 * StackTrace
 * @author Michael
 *
 */
@SuppressWarnings("serial")
public class TransparentException extends RuntimeException {

	public TransparentException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void printStackTrace(PrintStream str){
		getCause().printStackTrace(str);
	}
	
	@Override
	public void printStackTrace(PrintWriter wrtr){
		getCause().printStackTrace(wrtr);
	}

}
