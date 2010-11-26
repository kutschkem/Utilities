package kutschke.interpreter;

import static java.io.StreamTokenizer.TT_EOF;
import static java.io.StreamTokenizer.TT_NUMBER;
import static java.io.StreamTokenizer.TT_WORD;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import kutschke.generalStreams.GeneralInStream;
import kutschke.generalStreams.InStream;
import kutschke.higherClass.ReflectiveFun;

public class Parser extends NLParser{

	private char bracketOpen = '(';
	private char bracketClose = ')';
	
	public Parser(){
		super();
	}
	
	public Parser(int flags){
		super(flags);
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
					Object result = ProgramStreamIterator.NULL;
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
						IOException io = new IOException(
								"Exception occured while interpreting line "
								+ tokenizer.lineno(), se);
						io.setStackTrace(new StackTraceElement[]{});
						throw io;
					}
					return result;
			}
			
			@Override
			public void close() throws IOException{
				in.close();
			}
			
		};
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
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		return parser;
	}
	
}
