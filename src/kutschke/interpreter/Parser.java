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

import kutschke.generalStreams.GeneralInStream;
import kutschke.generalStreams.InStream;
import kutschke.generalStreams.iterators.StreamIterator;
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
	
	/**
	 * convenience method to parse the whole stream
	 * @param in
	 * @return
	 * @throws SyntaxException
	 * @throws IOException
	 */
	public Object parse(Reader in) throws SyntaxException, IOException{
		StreamIterator<Object> streamIt = new StreamIterator<Object>(stream(in));
		Object result = null;
		while(streamIt.hasNext()){
			result = streamIt.next();
		}
		return result;
	}


	/**
	 * returns a parsing stream that returns every top-level result
	 * @param in
	 * @return
	 */
	public InStream<Object> stream(final Reader in){
		return new GeneralInStream<Object>(){
			StreamTokenizer tokenizer = new StreamTokenizer(in);
			final char openBracket = getBracketOpen();
			final char closeBracket = getBracketClose();
			final int flags = getFlags();

			{
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

			}
			
			@Override
			public Object read() throws IOException {
					Object result = InStream.NULL;
					int brackets = 0;
					
					try{
					if((flags & BEGIN) != 0)
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
							if(brackets == 0)
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
					if((flags & END) != 0)
					interpreter.end();
					}catch(SyntaxException se){
						throw new IOException("Exception occured while interpreting line "
								+ tokenizer.lineno(),se);
					}
					return result;
			}
			
			@Override
			public void close() throws IOException{
				in.close();
			}
			
		};
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
