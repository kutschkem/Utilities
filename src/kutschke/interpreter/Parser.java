package kutschke.interpreter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;

import static java.io.StreamTokenizer.*;

public class Parser {

	private char bracketOpen = '(';
	private char bracketClose = ')';
	private Interpreter interpreter;
	private boolean greedy = true;
	
	/**
	 * @param bracketOpen the bracketOpen to set
	 */
	public void setBracketOpen(char bracketOpen) {
		this.bracketOpen = bracketOpen;
	}

	
	protected char getBracketOpen() {
		return bracketOpen;
	}
	/**
	 * @param bracketClose the bracketClose to set
	 */
	public void setBracketClose(char bracketClose) {
		this.bracketClose = bracketClose;
	}

	
	protected char getBracketClose() {
		return bracketClose;
	}
	
	public void parse(InputStream in) throws SyntaxException, IOException{
		StreamTokenizer tokenizer = new StreamTokenizer(new InputStreamReader(new BufferedInputStream(in)));
		final char openBracket = getBracketOpen();
		final char closeBracket = getBracketClose();
		int brackets = 0;
		
		tokenizer.parseNumbers();
		tokenizer.wordChars('_', '_');
		try{
		interpreter.begin();
		while(tokenizer.nextToken() != TT_EOF){
			if(tokenizer.ttype == openBracket){
				interpreter.openBracket();
				brackets ++;
			}
			else if(tokenizer.ttype == closeBracket){
				brackets --;
				if(brackets < 0)
					throw new SyntaxException("Missing closing bracket");
				interpreter.closeBracket();
				if(brackets == 0 && ! greedy)
					break;
			}
			else
			switch(tokenizer.ttype){
			case TT_NUMBER:
				interpreter.token(tokenizer.nval);
				break;
			case TT_WORD:	
				interpreter.token(tokenizer.sval);
				break;
			default:
				interpreter.special((char) tokenizer.ttype);
				
			}
		}
		if(brackets > 0)
			throw new SyntaxException("Too many closing brackets");
		interpreter.end();
		}catch(SyntaxException se){
			throw new SyntaxException("Exception occured while interpreting line "
					+ tokenizer.lineno(),se);
		}
	}


	public Interpreter getInterpreter() {
		return interpreter;
	}


	public void setInterpreter(Interpreter interpreter) {
		this.interpreter = interpreter;
	}


	public boolean isGreedy() {
		return greedy;
	}


	public void setGreedy(boolean greedy) {
		this.greedy = greedy;
	}
	
}
