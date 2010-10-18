package kutschke.interpreter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import kutschke.higherClass.Binding;
import kutschke.higherClass.GeneralOperation;

/**
 * A Lip-style interpreter. This means, parsers should call the Interpreters methods in
 * this order:<ul>
 * <li>start</li>
 * </ul>
 * <ul>
 * <li>openBracket</li>
 * <li> token or special </li>
 * <li> closeBracket </li>
 * </ul>
 * <ul>
 * <li>end</li>
 * </ul>
 * 
 * The first token after an open bracket is supposed to be the function identifier.
 * Note that if methods return GeneralOperations, that is okay as well.
 * @author Michael
 *
 */
public class LispStyleInterpreter implements Interpreter {

	protected Stack<List<Object>> methodStack = new Stack<List<Object>>();
	protected Deque<Map<String, GeneralOperation<Object, ?>>> scopes = new ArrayDeque<Map<String, GeneralOperation<Object, ?>>>();
	protected List<Object> actual = null;
	protected boolean DEBUG = false;
	protected Map<String,Interpreter> delegates = new HashMap<String,Interpreter>();
	protected String state = null;
	
	public Binding<Object,?> addMethod(String name, GeneralOperation<Object, ?> method) {
		if (scopes.isEmpty())
			scopes.push(new HashMap<String, GeneralOperation<Object, ?>>());
		Binding<Object,?> binding = new SealableBinding(method);
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
	
	protected void optimize(){
		for(Map<String,GeneralOperation<Object,?>> map : scopes){
			for(String method : map.keySet()){
				GeneralOperation<Object,?> op = map.get(method);
				if(op instanceof SealableBinding){
					SealableBinding _op = (SealableBinding) op;
					_op.seal();
					if(_op.getBindings().isEmpty()) // remove indirection
						map.put(method, _op.getInner());					
				}
			}
		}
		if(delegates.isEmpty())
			delegates = Collections.emptyMap();
	}

	@Override
	public void begin() throws SyntaxException {
		optimize();
		if(state != null){
			delegates.get(state).begin();
		}

	}

	@Override
	public Object closeBracket() throws SyntaxException {
		GeneralOperation<Object, ?> method;
		
		if(state != null)
			return delegates.get(state).closeBracket();
		if(actual.get(0) instanceof GeneralOperation){
			method = (GeneralOperation<Object, ?>) actual.get(0);
		}
		else method = getMapping(actual.get(0).toString());
		if (method == null)
			throw new SyntaxException("Unknown method " + actual.get(0));
		Object result = null;
		if(isDEBUG())
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
		return result;
	}

	@Override
	public void end() throws SyntaxException {
		if(state != null)
			delegates.get(state).end();

	}

	@Override
	public void openBracket() throws SyntaxException {
		if(state == null){
		methodStack.push(actual);
		actual = new ArrayList<Object>();
		}
		else
			delegates.get(state).openBracket();

	}

	@Override
	public void special(char special) throws SyntaxException {
		if(state == null)
			actual.add(special);
		else delegates.get(state).special(special);
	}

	@Override
	public void token(Object t) throws SyntaxException {
		if( actual.isEmpty() && delegates.keySet().contains(t)){
			changeState((String) t);
			actual = methodStack.pop();
		}
		actual.add(t);
	}
	
	public void changeState(String state){
		if(! delegates.keySet().contains(state))
			throw new RuntimeException(state +" is not a valid state");
		this.state = state;
	}

	public boolean isDEBUG() {
		return DEBUG;
	}

	public void setDEBUG(boolean debug) {
		DEBUG = debug;
	}

}
