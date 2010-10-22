package TestClasses;

import static org.junit.Assert.*;

import java.util.List;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import kutschke.interpreter.LispStyleInterpreter;
import kutschke.interpreter.Parser;
import kutschke.interpreter.SyntaxException;

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
		System.out.println(result);
		
	}

}
