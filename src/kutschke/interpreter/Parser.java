package kutschke.interpreter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kutschke.higherClass.Lambda;
import kutschke.higherClass.ReflectiveFun;

import static java.io.StreamTokenizer.*;

public class Parser {

	private char bracketOpen = '(';
	private char bracketClose = ')';
	private Interpreter interpreter;
	private boolean greedy = true;
	
	private List<Character> ordinary = new ArrayList<Character>();
	private List<Character> wordchars = new ArrayList<Character>();
	private List<Character> quoteChars = new ArrayList<Character>();
	private Map<String, Lambda<String,?>> converters = new LinkedHashMap<String,Lambda<String,?>>();
	
	public void ordinaryChar(char c){
		ordinary.add(c);
	}
	
	public void wordChar(char c){
		wordchars.add(c);
	}
	
	public void quoteChar(char c){
		quoteChars.add(c);
	}
	
	public void addConverter(String pattern, Lambda<String,?> converter){
		converters.put(pattern, converter);
	}
	
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
		
		for(Character c : ordinary){
			tokenizer.ordinaryChar(c);
		}
		for(Character c : wordchars){
			tokenizer.wordChars(c, c);
		}
		for(Character c : quoteChars){
			tokenizer.quoteChar(c);
		}
		
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
				interpreter.token(convert(tokenizer.sval));
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


	private Object convert(String sval) {
		for(String pattern : converters.keySet()){
			if(sval.matches(pattern))
				try {
					return converters.get(pattern).apply(sval);
				} catch (Exception e) {
					e.printStackTrace();
					return sval;
				}
		}
		return sval;
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
	
	public static Parser standardParser(){
		Parser parser = new Parser();
		parser.wordChar('_');
		parser.wordChar('.');
		parser.wordChar('-');
		
		try {
			parser.addConverter("[+\\-]?\\d+(\\.\\d+)?", new ReflectiveFun<Number>("parseDouble",Double.class,String.class).singleParameterAdapter(String.class));
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return parser;
	}
	
}
