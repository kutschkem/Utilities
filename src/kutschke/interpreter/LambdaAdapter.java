package kutschke.interpreter;

import java.util.ArrayList;
import java.util.List;

import kutschke.higherClass.Binding;
import kutschke.higherClass.GeneralOperation;
import kutschke.interpreter.abstractSyntax.AbSBuilder;
import kutschke.interpreter.abstractSyntax.AbSParser;

public class LambdaAdapter implements Interpreter {

	private Interpreter delegate;

	private static enum states {
		DEFAULT, LAMBDA_STATE, PARAM_STATE, FUNCTION_STATE, FINAL_STATE
	}

	private states state = states.DEFAULT;
	private String lambdaName = "lambda";
	private boolean lastWasOpenBracket = false;
	private List<Object> params;
	private int lambdaDepth = -1;
	private AbSParser parser;
	private AbSBuilder builder = new AbSBuilder();

	/**
	 * Beware: cyclic dependency is necessary here!!
	 * 
	 * @param adaptee
	 * @param parser
	 *            a parser that, when running, has been assigned the top-level
	 *            interpreter as interpreter
	 */
	public LambdaAdapter(Interpreter adaptee, AbSParser parser) {
		delegate = adaptee;
		this.parser = parser;
	}

	@Override
	public void begin() throws SyntaxException {
		if (delegate.getMapping(lambdaName) == null)
			delegate.addMethod("lambda", new Identity<Object>());
		delegate.begin();

	}

	@Override
	public Object closeBracket() throws SyntaxException {
		switch (state) {
		case DEFAULT:
			return delegate.closeBracket();
		case LAMBDA_STATE:
			throw new SyntaxException("Misplaced closing bracket");
		case PARAM_STATE:
			state = states.LAMBDA_STATE;
			break;
		case FUNCTION_STATE:
			if (lambdaDepth == 0)
				state = states.FINAL_STATE;
			lambdaDepth--;
			builder.closeBracket();
			break;
		case FINAL_STATE:
			state = states.DEFAULT;
			lambdaDepth = -1;
			Object result = makeLambda(params);
			delegate.token(result);
			delegate.closeBracket();
			params = null;
			builder.clear();
			return result;
		}
		return null;
	}

	private Object makeLambda(List<Object> params) {
		return new LambdaOperation(parser, params, builder.getAbstractSyntax());
	}

	@Override
	public void end() throws SyntaxException {
		delegate.end();

	}

	@Override
	public void openBracket() throws SyntaxException {
		lastWasOpenBracket = true;
		switch (state) {
		case DEFAULT:
			delegate.openBracket();
			break;
		case LAMBDA_STATE:
			if (params == null) {
				state = states.PARAM_STATE;
				params = new ArrayList<Object>();
			} else {
				state = states.FUNCTION_STATE;
				builder.clear();
				builder.openBracket();
				lambdaDepth = 0;
			}
			break;
		case PARAM_STATE:
			throw new SyntaxException(
					"Expected Parameter declaration, but found '('");
		case FUNCTION_STATE:
			builder.openBracket();
			lambdaDepth++;
			break;
		case FINAL_STATE:
			throw new SyntaxException(
					"Misplaced opening bracket, expected closing bracket");
		}
	}

	@Override
	public void special(char special) throws SyntaxException {
		lastWasOpenBracket = false;
		switch (state) {
		case DEFAULT:
			delegate.special(special);
			break;
		default:
			token(special);
		}

	}

	@Override
	public void token(Object t) throws SyntaxException {
		switch (state) {
		case DEFAULT:
			if (lastWasOpenBracket && t.equals(lambdaName)) {
				state = states.LAMBDA_STATE;
			}
			delegate.token(t);
			break;
		case LAMBDA_STATE:
			throw new SyntaxException("expected ( but found: " + t);
		case PARAM_STATE:
			params.add(t);
			break;
		case FUNCTION_STATE:
			if (params.contains(t)) {
				builder.openBracket();
				builder.token("__params");
				builder.token(t);
				builder.closeBracket();
			} else {
				Object temp = delegate.getMapping(t.toString()); // account for
																	// scoping
																	// issues
				// btw. this is also a little optimization
				if (t instanceof String && temp != null)
					builder.token(temp);
				else
					builder.token(t);
			}

			break;
		case FINAL_STATE:
			throw new SyntaxException("Expected closing bracket, found: " + t);
		}
		lastWasOpenBracket = false;

	}

	/**
	 * @param lambdaName
	 *            the lambdaName to set
	 */
	public void setLambdaName(String lambdaName) {
		this.lambdaName = lambdaName;
	}

	/**
	 * @return the lambdaName
	 */
	public String getLambdaName() {
		return lambdaName;
	}

	@Override
	public Binding<Object, ?> addMethod(String name,
			GeneralOperation<Object, ?> method) {
		return delegate.addMethod(name, method);
	}

	@Override
	public GeneralOperation<Object, ?> getMapping(String methodName) {
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

	@Override
	public List<Object> getActualParameters() {
		return delegate.getActualParameters();

	}

}
