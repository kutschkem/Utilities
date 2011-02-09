package kutschke.higherClass;

/**
 * an interface for the most general of all Operations, can be applied to any
 * amount of parameters
 * 
 * @author Michael
 * 
 * @param <ArgType> the type of the Arguments
 * @param <ResultType> the type of the results
 */
public interface GeneralOperation<ArgType, ResultType, ExceptionType extends Exception> extends
		Lambda<ArgType[], ResultType,ExceptionType> {

}
