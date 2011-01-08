package kutschke.interpreter;

import kutschke.higherClass.GeneralOperation;

public class Identity<T> implements GeneralOperation<T, T> {

	@Override
	public T apply(T[] arg) throws Exception {
		return arg[arg.length - 1];
	}

}
