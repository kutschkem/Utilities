package kutschke.interpreter;

import kutschke.higherClass.GeneralOperation;

public class Identity<T> implements GeneralOperation<T, T,ArrayIndexOutOfBoundsException> {

	@Override
	public T apply(T[] arg) {
		return arg[arg.length - 1];
	}

}
