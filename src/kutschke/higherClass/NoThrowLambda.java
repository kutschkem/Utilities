package kutschke.higherClass;

public interface NoThrowLambda<ArgType, ResultType> extends Lambda<ArgType, ResultType> {

	@Override
	public ResultType apply(ArgType arg);
}
