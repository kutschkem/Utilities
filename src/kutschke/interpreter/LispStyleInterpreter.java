package kutschke.interpreter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A Lip-style interpreter. This means, parsers should call the Interpreters
 * methods in this order:
 * <ul>
 * <li>start</li>
 * </ul>
 * <ul>
 * <li>openBracket</li>
 * <li>token or special</li>
 * <li>closeBracket</li>
 * </ul>
 * <ul>
 * <li>end</li>
 * </ul>
 * 
 * The first token after an open bracket is supposed to be the function
 * identifier. Note that if methods return GeneralOperations, that is okay as
 * well.
 * 
 * @author Michael
 * 
 */
public class LispStyleInterpreter extends SimpleInterpreter {

	protected Map<String, Interpreter> delegates = new HashMap<String, Interpreter>();
	protected String state = null;
	protected int delegationDepth = -1;

	@Override
	public LispStyleInterpreter optimize() {
		super.optimize();
		if (delegates.isEmpty())
			delegates = Collections.emptyMap();
		return this;
	}

	@Override
	public void begin() throws SyntaxException {
		if (state != null) {
			delegates.get(state).begin();
		}

	}

	@Override
	public Object closeBracket() throws SyntaxException {
		if (state != null)
			{
			Object result = delegates.get(state).closeBracket();
			delegationDepth --;
			if(delegationDepth == -1){
				state = null;
				actual.add(result);
			}
			return result;
			}
		return super.closeBracket();
	}

	@Override
	public void end() throws SyntaxException {
		if (state != null)
			delegates.get(state).end();

	}

	@Override
	public void openBracket() throws SyntaxException {
		if (state == null) {
			super.openBracket();
		} else{
			delegationDepth ++;
			delegates.get(state).openBracket();
		}

	}

	@Override
	public void special(char special) throws SyntaxException {
		if (state == null)
			super.special(special);
		else
			delegates.get(state).special(special);
	}

	@Override
	public void token(Object t) throws SyntaxException {
		if (state != null) {
			delegates.get(state).token(t);
		} else {
			if (actual.isEmpty() && delegates.keySet().contains(t)) {
				changeState((String) t);
				actual = methodStack.pop();
				delegationDepth = 0;
			}
			super.token(t);
		}
	}

	public void changeState(String state) throws SyntaxException {
		if (!delegates.keySet().contains(state))
			throw new RuntimeException(state + " is not a valid state");
		this.state = state;
		delegates.get(state).begin();
		delegates.get(state).openBracket();
		delegates.get(state).token(state);
	}

}
