package kutschke.interpreter;

import java.util.ArrayList;
import java.util.List;

import kutschke.higherClass.Binding;
import kutschke.higherClass.GeneralOperation;
import kutschke.higherClass.Identity;

public class LambdaAdapter implements Interpreter {

	private Interpreter delegate;
	private final static int LAMBDA_STATE = 1;
	private final static int PARAM_STATE = 2;
	private final static int FUNCTION_STATE = 4;
	private final static int FINAL_STATE = 8;
	private int state = 0;
	private String lambdaName = "lambda";
	private boolean lastWasOpenBracket = false;
	private StringBuilder builder;
	private List<Object> params;
	private int lambdaDepth = -1;
	private Parser parser;

	public LambdaAdapter(Interpreter adaptee, Parser parser) {
		delegate = adaptee;
		this.parser = parser;
	}

	@Override
	public void begin() throws SyntaxException {
		if(delegate.getMapping(lambdaName) == null)
		delegate.addMethod("lambda", new Identity<Object>());
		delegate.begin();

	}

	@Override
	public Object closeBracket() throws SyntaxException {
		switch(state){
		case 0:
			return delegate.closeBracket();
		case LAMBDA_STATE:
			throw new SyntaxException("Misplaced closing bracket");
		case PARAM_STATE:
			state = LAMBDA_STATE;
			break;
		case FUNCTION_STATE:
			lambdaDepth --;
			if(lambdaDepth == 0)
				state = FINAL_STATE;
			builder.append(parser.getBracketClose());
			break;
		case FINAL_STATE:
			state = 0;
			lambdaDepth = -1;
			Object result = makeLambda(params,builder.toString());
			delegate.token(result);
			delegate.closeBracket();
			params = null;
			builder = null;
			return result;
		}
		return null;
	}

	private Object makeLambda(List<Object> params2, String string) {
		return new LambdaOperation(parser,delegate,params2,string);
	}

	@Override
	public void end() throws SyntaxException {
		delegate.end();

	}

	@Override
	public void openBracket() throws SyntaxException {
		switch (state) {
		case 0:
			delegate.openBracket();
			lastWasOpenBracket = true;
			break;
		case LAMBDA_STATE:
			if (params == null) {
				state = PARAM_STATE;
				params = new ArrayList<Object>();
			} else {
				state = FUNCTION_STATE;
				builder = new StringBuilder();
				builder.append(parser.getBracketOpen());
				lambdaDepth = 0;
			}
			break;
		case PARAM_STATE:
			throw new SyntaxException(
					"Expected Parameter declaration, but found '('");
		case FUNCTION_STATE:
			builder.append(parser.getBracketOpen());
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
		case 0:
			delegate.special(special);
			break;
		case LAMBDA_STATE:
			throw new SyntaxException("Expected ( but found: " + special);
		case PARAM_STATE:
			params.add(special);
			break;
		case FUNCTION_STATE:
			if (params.contains(special)) {
				builder.append(parser.getBracketOpen());
				builder.append("__params ");
				builder.append(special);
				builder.append(parser.getBracketClose());
			} else {
				builder.append(" ");
				builder.append(special);
			}
			break;
		case FINAL_STATE:
			throw new SyntaxException("Expected closing bracket, found: "
					+ special);
		}

	}

	@Override
	public void token(Object t) throws SyntaxException {
		switch (state) {
		case 0:
			if (lastWasOpenBracket && t.equals(lambdaName)) {
				state = LAMBDA_STATE;
				lastWasOpenBracket = false;
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
				builder.append(parser.getBracketOpen());
				builder.append("__params ");
				builder.append(t);
				builder.append(parser.getBracketClose());
			} else {
				builder.append(" ");
				builder.append(t);
			}

			break;
		case FINAL_STATE:
			throw new SyntaxException("Expected closing bracket, found: " + t);
		}

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
