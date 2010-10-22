package kutschke.interpreter;

import java.io.StringReader;
import java.util.List;

import kutschke.higherClass.Binding;
import kutschke.higherClass.GeneralOperation;
import kutschke.higherClass.ReflectiveFun;

public class LambdaOperation implements GeneralOperation<Object, Object> {

	Parser parser;
	Interpreter interpreter;
	List<Object> params;
	String function;
	
	public LambdaOperation(Parser parser, Interpreter interpreter, List<Object> params, String function){
		this.parser = parser;
		this.interpreter = interpreter;
		this.params = params;
		this.function = function;
	}

	@Override
	public Object apply(Object[] arg) throws Exception {
		interpreter.pushScope();
		interpreter.addMethod("__params", new Binding<Object,Object>(
				new ReflectiveFun<Object>("getParamByName", getClass(),
						Object[].class, Object.class).setBound(this)).bind(0,
				arg));
		int parserflags = parser.getFlags();
		parser.setFlags(0);
		Object result = parser.parse(new StringReader(function));
		interpreter.getActualParameters().remove(interpreter.getActualParameters().size() - 1);
		/*
		 * removing is necessary because otherwise the result will get into the
		 * parameters twice
		 */
		interpreter.popScope();
		parser.setFlags(parserflags);
		return result;
	}

	public Object getParamByName(Object[] arg, Object name) {
		return arg[params.indexOf(name)];
	}

}
