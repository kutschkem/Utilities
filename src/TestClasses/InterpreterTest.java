package TestClasses;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import kutschke.higherClass.GeneralOperation;
import kutschke.higherClass.ReflectiveFun;
import kutschke.interpreter.Interpreter;
import kutschke.interpreter.LambdaAdapter;
import kutschke.interpreter.LispStyleInterpreter;
import kutschke.interpreter.LocalAdapter;
import kutschke.interpreter.Parser;
import kutschke.interpreter.SyntaxException;
import kutschke.interpreter.abstractSyntax.AbSParser;

public class InterpreterTest {
	
	@Test
	public void testMetaInterpreter() throws SyntaxException, IOException{
		String program = "(define list\n"
			+"(local (define fun (ctor (classForName kutschke.higherClass.ReflectiveFun) (classForName java.lang.String) (classForName java.lang.Class) (classForName [Ljava.lang.Class;)))"
			+"(fun asList (classForName java.util.Arrays) (classForName [Ljava.lang.Object;))))\n"
			+ "(list a b c) ";
		Parser parser = Parser.standardParser();
		parser.setFlags(Parser.BEGIN|Parser.END|Parser.COMMENTS);
		parser.setInterpreter(LispStyleInterpreter.metaInterpreter());
		Object result = parser.parse(new StringReader(program));
		assertTrue(result instanceof List);
		assertArrayEquals(((List)result).toArray(), new Object[]{"a","b","c"});
		System.out.println(result);
		
		program = "(define foobar (lambda (a b) (list a b))) (foobar (foobar c d) (foobar e f))";
		result = parser.parse(new StringReader(program));
		System.out.println(result);
		assertTrue(result instanceof List);
		assertArrayEquals(((List)result).toArray(), new Object[]{Arrays.asList("c","d"), Arrays.asList("e","f")});

	}
	
	@Test
	public void testLambdaIssues() throws SyntaxException, IOException, SecurityException, NoSuchMethodException{
		String program = "(define bar (lambda (a b) (local (define foo asList) (foo a b))))" +
				"(bar 1 2)";
		Parser parser = Parser.standardParser();
		parser.setFlags(Parser.BEGIN|Parser.END|Parser.COMMENTS);
		AbSParser p2 = new AbSParser();
		Interpreter interp = new LambdaAdapter(new LocalAdapter(new LispStyleInterpreter()), p2);
		p2.setInterpreter(interp);
		parser.setInterpreter(interp);
		
		interp.addMethod("define", new ReflectiveFun<Void>("addMethod",Interpreter.class,String.class, GeneralOperation.class).setBound(interp));
		
		interp.addMethod("foo", new GeneralOperation<Object,Object>(){

			@Override
			public Object apply(Object[] arg) throws Exception {
				return Integer.valueOf((String) arg[0]) + Integer.valueOf((String) arg[1]);
			}
			
		});
		interp.addMethod("asList", new ReflectiveFun<List<?>>("asList",Arrays.class,Object[].class));
		
		Object result = parser.parse(new StringReader(program));
		assertTrue(result instanceof List);

	}

}