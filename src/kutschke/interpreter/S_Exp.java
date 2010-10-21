package kutschke.interpreter;

import interfaces.Optimizable;

import java.util.Queue;

public interface S_Exp extends Queue<Object>, Optimizable<Object> {

	public Object evaluate() throws SyntaxException;
	
}
