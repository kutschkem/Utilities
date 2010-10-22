package kutschke.interpreter;

import java.util.HashMap;
import java.util.Map;

public class S_ExpBuilder extends LispStyleInterpreter {

	protected Map<String,Class<? extends S_Exp>> S_Expmap = new HashMap<String,Class<? extends S_Exp>>();

	@Override
	public S_Exp closeBracket() throws SyntaxException {
		S_Exp sexp = null;
		if(S_Expmap.containsKey(this.actual.get(0))){
			try {
				sexp = S_Expmap.get(actual.get(0)).newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else sexp = new GenericS_Exp();
		sexp.addAll(actual);
		actual = methodStack.pop();
		actual.add(sexp);
		return sexp;
	}

}
