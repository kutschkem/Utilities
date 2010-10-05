package kutschke.exceptions;

public class ExceptionWrapper extends RuntimeException {

	
	/**
	 * generated SerialVersion ID
	 */
	private static final long serialVersionUID = -7402081430878745085L;
	
	
	protected final Exception wrapped;
	
	public Exception getWrapped() {
		return wrapped;
	}

	public ExceptionWrapper(Exception ex){
		wrapped = ex;
	}
	
	
}
