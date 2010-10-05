package kutschke.higherClass;

/**
 * interface representing an Operation that can be applied to an argument
 * 
 * @author Michael
 * 
 * @param <ArgType>
 *            the type of the Argument
 * @param <ResultType>
 *            the type of the result
 */
public interface Lambda<ArgType, ResultType> {

	/**
	 * apply the encapsulated Operation on the Argument
	 * 
	 * @param arg
	 *            the Argument
	 * @return the result of the Operation
	 */
	public ResultType apply(ArgType arg);

}
