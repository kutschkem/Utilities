package kutschke.interpreter.abstractSyntax;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import kutschke.higherClass.Binding;
import kutschke.higherClass.GeneralOperation;
import kutschke.interpreter.Interpreter;
import kutschke.interpreter.SyntaxException;
import static kutschke.interpreter.abstractSyntax.AbSParser.SPECIAL.*;

/**
 * This class is for building / "compiling" (of some sort) the abstract syntax.
 * Used for example for lambda expressions
 * @author Michael
 *
 */
public class AbSBuilder implements Interpreter {

	List<Object> absList = new LinkedList<Object>();
	
	@Override
	public void begin() throws SyntaxException {
		// TODO Auto-generated method stub

	}

	@Override
	public void openBracket() throws SyntaxException {
		absList.add(bracketOpen);

	}

	@Override
	public Object closeBracket() throws SyntaxException {
		absList.add(bracketClose);
		return null;
	}

	@Override
	public void token(Object t) throws SyntaxException {
		absList.add(t);

	}

	@Override
	public void special(char special) throws SyntaxException {
		absList.add(special);

	}

	@Override
	public void end() throws SyntaxException {
		// TODO Auto-generated method stub

	}

	@Override
	public void pushScope() {
		absList.add(pushScope);

	}

	@Override
	public void popScope() {
		absList.add(popScope);

	}

	@Override
	public Binding<Object, ?> addMethod(String name,
			GeneralOperation<Object, ?,?> method) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GeneralOperation<Object, ?,?> getMapping(String methodName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<?> getAbstractSyntax(){
		return new ArrayList<Object>(absList);
	}
	
	public void clear(){
		absList.clear();
	}

}
