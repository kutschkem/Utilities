package kutschke.interpreter;

import kutschke.higherClass.GeneralOperation;
import interfaces.SideEffects;

public class Identity<T> implements GeneralOperation<T, T> {

	@SideEffects(false)
	@Override
	public T apply(T[] arg) throws Exception {
		return arg[arg.length - 1];
	}

}
