package kutschke.interpreter;

import java.util.Stack;

import kutschke.higherClass.Binding;
import kutschke.higherClass.GeneralOperation;

public class LocalAdapter implements Interpreter {

	protected Interpreter delegate;
	private String localName = "local";
	protected boolean lastWasOpenbracket = false;
	private Stack<Integer> localDepths = new Stack<Integer>();

	public LocalAdapter(Interpreter adaptee) {
		delegate = adaptee;
	}

	@Override
	public void begin() throws SyntaxException {
		delegate.begin();
	}

	@Override
	public Object closeBracket() throws SyntaxException {
		lastWasOpenbracket = false;
		if(! localDepths.isEmpty()){
			if(localDepths.peek() == 0){
				localDepths.pop();
				delegate.popScope();
			}
			else
				localDepths.push(localDepths.pop() - 1);
		}
		return delegate.closeBracket();
	}

	@Override
	public void end() throws SyntaxException {
		delegate.end();

	}

	@Override
	public void openBracket() throws SyntaxException {
		delegate.openBracket();
		lastWasOpenbracket = true;
		if(! localDepths.isEmpty()){
			localDepths.push(localDepths.pop() + 1);
		}
	}

	@Override
	public void special(char special) throws SyntaxException {
		delegate.special(special);
		lastWasOpenbracket = false;
	}

	@Override
	public void token(Object t) throws SyntaxException {
		if (lastWasOpenbracket && t.equals(getLocalName())){
			delegate.pushScope();
			localDepths.push(0);
			t = new Identity<Object>(); // avoid registering "local" as function name
		}
		lastWasOpenbracket = false;
		delegate.token(t);
	}

	/**
	 * @param localName
	 *            the localName to set
	 */
	public void setLocalName(String localName) {
		this.localName = localName;
	}

	/**
	 * @return the localName
	 */
	public String getLocalName() {
		return localName;
	}

	@Override
	public Binding<Object, ?> addMethod(String name,
			GeneralOperation<Object, ?,?> method) {
		return delegate.addMethod(name, method);
	}

	@Override
	public GeneralOperation<Object, ?,?> getMapping(String methodName) {
		return delegate.getMapping(methodName);
	}

	@Override
	public void popScope() {
		delegate.popScope();
		
	}

	@Override
	public void pushScope() {
		delegate.pushScope();
		
	}

}
