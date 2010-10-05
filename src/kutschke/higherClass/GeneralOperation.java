package kutschke.higherClass;

import java.util.Collection;

/**
 * an interface for the most general of all Operations, can be applied to any
 * amount of parameters (either in a collection or an array
 * 
 * @author Michael
 * 
 * @param <ArgType> the type of the Arguments
 * @param <ResultType> the type of the results
 */
public interface GeneralOperation<ArgType, ResultType> extends
		Lambda<Collection<ArgType>, ResultType> {

	public ResultType apply(ArgType[] args);
}
