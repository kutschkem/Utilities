package kutschke.interpreter.structural;


import java.util.Queue;

import kutschke.interfaces.Optimizable;
import kutschke.interpreter.SyntaxException;

public interface S_Exp extends Queue<Object>, Optimizable<Object> {

	public Object evaluate() throws SyntaxException;
	
}
