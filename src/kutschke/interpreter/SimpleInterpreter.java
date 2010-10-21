package kutschke.interpreter;

import interfaces.Optimizable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import kutschke.higherClass.Binding;
import kutschke.higherClass.GeneralOperation;

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
 * Note that this interpreter does not support (on it's own) scopes or delegation.
 * Use this Interpreter for simple Use cases, e.g. parsing Network messages.
 * 
 * @author Michael
 * 
 */
public class SimpleInterpreter implements Interpreter,
		Optimizable<SimpleInterpreter> {

	protected Stack<List<Object>> methodStack = new Stack<List<Object>>();
	protected Deque<Map<String, GeneralOperation<Object, ?>>> scopes = new ArrayDeque<Map<String, GeneralOperation<Object, ?>>>();
	protected List<Object> actual = null;
	protected boolean DEBUG = false;

	public Binding<Object, ?> addMethod(String name,
			GeneralOperation<Object, ?> method) {
		if (scopes.isEmpty())
			scopes.push(new HashMap<String, GeneralOperation<Object, ?>>());
		Binding<Object, ?> binding = new SealableBinding(method);
		scopes.peek().put(name, binding);
		return binding;
	}

	public GeneralOperation<Object, ?> getMapping(String methodName) {
		for (Map<String, GeneralOperation<Object, ?>> scope : this.scopes) {
			if (scope.containsKey(methodName))
				return scope.get(methodName);
		}
		return null;
	}

	public SimpleInterpreter optimize() {
		for (Map<String, GeneralOperation<Object, ?>> map : scopes) {
			for (String method : map.keySet()) {
				GeneralOperation<Object, ?> op = map.get(method);
				if (op instanceof SealableBinding) {
					SealableBinding _op = (SealableBinding) op;
					_op.seal();
					if (_op.getBindings().isEmpty()) // remove indirection
						map.put(method, _op.getInner());
				}
			}
		}
		return this;
	}

	@Override
	public void begin() throws SyntaxException {

	}

	@SuppressWarnings("unchecked")
	@Override
	public Object closeBracket() throws SyntaxException {
		GeneralOperation<Object, ?> method;
		if (actual.get(0) instanceof GeneralOperation) {
			method = (GeneralOperation<Object, ?>) actual.get(0);
		} else
			method = getMapping(actual.get(0).toString());
		if (method == null)
			throw new SyntaxException("Unknown method " + actual.get(0));
		Object result = null;
		if (isDEBUG()) {
			System.err.println("[DEBUG]: Interpreting " + actual);
			System.err.flush();
		}
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
		return result;
	}

	@Override
	public void end() throws SyntaxException {

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
