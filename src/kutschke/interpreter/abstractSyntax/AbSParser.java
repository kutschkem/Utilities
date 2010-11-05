package kutschke.interpreter.abstractSyntax;

import kutschke.interpreter.Interpreter;
import kutschke.interpreter.SyntaxException;

public class AbSParser {
	
	enum SPECIAL{
		bracketOpen,
		bracketClose,
		pushScope,
		popScope
	}

	private Interpreter interp;

	public void setInterpreter(Interpreter interp) {
		this.interp = interp;
	}

	public Interpreter getInterpreter() {
		return interp;
	}
	
	public Object parse(Iterable<?> abstracts) throws SyntaxException{
		Object result = null;
		for(Object o : abstracts){
			if(o instanceof SPECIAL)
				switch((SPECIAL) o){
				case bracketOpen: interp.openBracket();
				break;
				case bracketClose: result = interp.closeBracket();
				break;
				case pushScope: interp.pushScope();
				break;
				case popScope: interp.popScope();
				}
			else
				if(o instanceof Character)
				interp.special((Character)o);
				else interp.token(o);
		}

		return result;
	}
}
