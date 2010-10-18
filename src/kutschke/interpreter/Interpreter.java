package kutschke.interpreter;

/**
 * Interface for Interpreters to be used with the Parser class.<br/>
 * Note that by default, the parser does a check on matching opening and closing
 * brackets. Also note that a non-greedy Parser only evaluates one top-level expression
 * @author Michael
 *
 */
public interface Interpreter {

	/**
	 * called once before parsing
	 * 
	 * @throws SyntaxException
	 */
	public void begin() throws SyntaxException;

	/**
	 * called when an opening bracket is found
	 * 
	 * @throws SyntaxException
	 */
	public void openBracket() throws SyntaxException;

	/**
	 * called when a closing bracket is found
	 * 
	 * @throws SyntaxException
	 */
	public Object closeBracket() throws SyntaxException;

	/**
	 * called when a word or number token is found
	 * 
	 * @param t
	 * @throws SyntaxException
	 */
	public void token(Object t) throws SyntaxException;

	/**
	 * called when a non-word token is found
	 * 
	 * @param special
	 * @throws SyntaxException
	 */
	public void special(char special) throws SyntaxException;

	/**
	 * called at the end of parsing
	 * 
	 * @throws SyntaxException
	 */
	public void end() throws SyntaxException;

}
