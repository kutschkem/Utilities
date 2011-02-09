package kutschke.higherClass;

public interface NoThrowLambda<ArgType, ResultType> extends Lambda<ArgType, ResultType,Exception> {

	@Override
	public ResultType apply(ArgType arg);
}
