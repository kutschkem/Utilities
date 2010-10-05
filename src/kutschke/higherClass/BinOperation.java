package kutschke.higherClass;

/**
 * interface for a binary operation, that means the Operation has to parameters
 * 
 * @author Michael
 * 
 * @param <ArgType1>
 *            the type of the first Operand
 * @param <ArgType2>
 *            the type of the second Operand
 * @param <ResultType>
 *            the result of the Operation
 */
public interface BinOperation<ArgType1, ArgType2, ResultType> {

	/**
	 * apply the binary Operation to the Arguments
	 * 
	 * @param arg0
	 *            Argument 1
	 * @param arg1
	 *            Argument 2
	 * @return the result of the Operation
	 */
	public ResultType apply(ArgType1 arg0, ArgType2 arg1);

}
