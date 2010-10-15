package kutschke.interpreter;

public interface Interpreter {

	public void begin() throws SyntaxException;
	public void openBracket() throws SyntaxException;
	public void closeBracket() throws SyntaxException;
	public void token(Object t) throws SyntaxException;
	public void special(char special) throws SyntaxException;
	public void end() throws SyntaxException;
	
}
