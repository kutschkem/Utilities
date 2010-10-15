package kutschke.interpreter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import kutschke.higherClass.GeneralOperation;

public class LispStyleInterpreter implements Interpreter {

	protected Stack<List<Object>> methodStack = new Stack<List<Object>>();
	protected Deque<Map<String, GeneralOperation<Object, ?>>> scopes = new ArrayDeque<Map<String, GeneralOperation<Object, ?>>>();
	protected List<Object> actual = null;
	protected boolean DEBUG = false;
	

	public void addMethod(String name, GeneralOperation<Object, ?> method) {
		if (scopes.isEmpty())
			scopes.push(new HashMap<String, GeneralOperation<Object, ?>>());
		scopes.peek().put(name, method);
	}

	protected GeneralOperation<Object, ?> getMapping(String methodName) {
		for (Map<String, GeneralOperation<Object, ?>> scope : this.scopes) {
			if (scope.containsKey(methodName))
				return scope.get(methodName);
		}
		return null;
	}

	@Override
	public void begin() throws SyntaxException {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeBracket() throws SyntaxException {
		GeneralOperation<Object, ?> method = getMapping(actual.get(0).toString());
		if (method == null)
			throw new SyntaxException("Unknown method " + actual.get(0));
		Object result = null;
		if(DEBUG)
			System.err.println("[DEBUG]: Interpreting " + actual);
		System.err.flush();
		try {
			result = method.apply(actual.subList(1, actual.size()).toArray());
		} catch (Exception e) {
			if (e instanceof SyntaxException)
				throw (SyntaxException) e;
			throw new SyntaxException(e);
		}
		actual = methodStack.pop();
		if (actual != null)
			actual.add(result);
	}

	@Override
	public void end() throws SyntaxException {
		// TODO Auto-generated method stub

	}

	@Override
	public void openBracket() throws SyntaxException {
		methodStack.push(actual);
		actual = new ArrayList<Object>();

	}

	@Override
	public void special(char special) throws SyntaxException {
			actual.add(special);
	}

	@Override
	public void token(Object t) throws SyntaxException {
		actual.add(t);

	}

	public boolean isDEBUG() {
		return DEBUG;
	}

	public void setDEBUG(boolean debug) {
		DEBUG = debug;
	}

}
