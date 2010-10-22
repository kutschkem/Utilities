package kutschke.interpreter.structural;

import interfaces.Optimizable;

import java.util.Queue;

import kutschke.interpreter.SyntaxException;

public interface S_Exp extends Queue<Object>, Optimizable<Object> {

	public Object evaluate() throws SyntaxException;
	
}
