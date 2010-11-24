package kutschke.interpreter;

import java.util.List;

import kutschke.higherClass.Binding;
import kutschke.higherClass.GeneralOperation;
import kutschke.higherClass.ReflectiveFun;
import kutschke.interpreter.abstractSyntax.AbSParser;

public class LambdaOperation implements GeneralOperation<Object, Object> {

	AbSParser parser;
	List<Object> params;
	List<?> function;
	
	public LambdaOperation(AbSParser parser, List<Object> params, List<?> list){
		this.parser = parser;
		this.params = params;
		this.function = list;
	}

	@Override
	public Object apply(Object[] arg) throws Exception {
		parser.getInterpreter().pushScope();
		parser.getInterpreter().addMethod("__params", new Binding<Object,Object>(
				new ReflectiveFun<Object>("getParamByName", getClass(),
						Object[].class, Object.class).setBound(this)).bind(0,
				arg));
		Object result = parser.parse(function);

		parser.getInterpreter().popScope();
		return result;
	}

	public Object getParamByName(Object[] arg, Object name) {
		return arg[params.indexOf(name)];
	}
	
	@Override
	public String toString(){
		return "(lambda " + function + ")";
	}

}
