package kutschke.interpreter;

import static java.io.StreamTokenizer.TT_EOF;
import static java.io.StreamTokenizer.TT_NUMBER;
import static java.io.StreamTokenizer.TT_WORD;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kutschke.higherClass.Lambda;
import kutschke.higherClass.ReflectiveFun;

public class Parser {

	public final static int BEGIN = 1;
	public final static int END = 2;
	public final static int COMMENTS = 4;
	public final static int DEFAULT = -1;
	
	private char bracketOpen = '(';
	private char bracketClose = ')';
	private Interpreter interpreter;
	private boolean greedy = true;
	private int flags = DEFAULT;
	
	private List<Character> ordinary = new ArrayList<Character>();
	private List<Character> wordchars = new ArrayList<Character>();
	private List<Character> quoteChars = new ArrayList<Character>();
	private Map<String, Lambda<String,?>> converters = new LinkedHashMap<String,Lambda<String,?>>();
	
	public Parser(){
		
	}
	
	public Parser(int flags){
		setFlags(flags);
	}
	
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
	
	public Object parse(Reader in) throws SyntaxException, IOException{
		StreamTokenizer tokenizer = new StreamTokenizer(in);
		final char openBracket = getBracketOpen();
		final char closeBracket = getBracketClose();
		Object result = null;
		int brackets = 0;
		if((getFlags() & COMMENTS) == 0){
			tokenizer.slashSlashComments(false);
			tokenizer.slashStarComments(false);
		}
		
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
		if((getFlags() & BEGIN) != 0)
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
				result = interpreter.closeBracket();
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
		if((getFlags() & END) != 0)
		interpreter.end();
		}catch(SyntaxException se){
			throw new SyntaxException("Exception occured while interpreting line "
					+ tokenizer.lineno(),se);
		}
		return result;
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
		// wordchars for proper parsing of Classnames,  numbers etc.
		parser.wordChar('_');
		parser.wordChar('.');
		parser.wordChar('-');
		parser.wordChar('[');
		parser.wordChar(';');
		
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

	/**
	 * @param flags the flags to set
	 */
	public void setFlags(int flags) {
		this.flags = flags;
	}

	/**
	 * @return the flags
	 */
	public int getFlags() {
		return flags;
	}
	
}
