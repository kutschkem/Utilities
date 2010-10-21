package kutschke.higherClass;

import interfaces.SideEffects;

public class Identity<T> implements GeneralOperation<T, T> {

	@SideEffects(false)
	@Override
	public T apply(T[] arg) throws Exception {
		return arg[arg.length-1];
	}

}
