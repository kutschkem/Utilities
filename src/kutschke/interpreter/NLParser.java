package kutschke.interpreter;

import static java.io.StreamTokenizer.*;

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

public class NLParser {
	
	/**
	 * this Iterator differs from it's superclass in permitting null values in the Stream.
	 * Streams that are supposed to be used with this Iterator should return 
	 * ProgramStreamIterator.NULL to mark their end. Try not to have any program return this
	 * as top-level return value, as it will mess up things
	 * @author Michael
	 *
	 */
	public static class ProgramStreamIterator extends StreamIterator<Object>{

		public static final Object NULL = new Object();
		
		public ProgramStreamIterator(InStream<Object> str) {
			super(str);
		}
		
		@Override
		public boolean hasNext(){
			try {
				buffer = inStr.read();
				checked = true;
			} catch (Exception e) {
				return false;
			}
			return buffer != NULL;
		}
		
	}

	public final static int BEGIN = 1;
	public final static int END = 2;
	public final static int COMMENTS = 4;
	public final static int DEFAULT = -1;
	protected Interpreter interpreter;
	private int flags = DEFAULT;
	protected List<Character> ordinary = new ArrayList<Character>();
	protected List<Character> wordchars = new ArrayList<Character>();
	protected List<Character> quoteChars = new ArrayList<Character>();
	private Map<String, Lambda<String, ?>> converters = new LinkedHashMap<String, Lambda<String, ?>>();

	public NLParser() {
	}

	public NLParser(int flags) {
		setFlags(flags);
	}

	public InStream<Object> stream(final Reader in) {
		return new GeneralInStream<Object>() {
			StreamTokenizer tokenizer = new StreamTokenizer(in);
			final int flags = getFlags();
			boolean begun = false;

			{
				if ((getFlags() & COMMENTS) == 0) {
					tokenizer.slashSlashComments(false);
					tokenizer.slashStarComments(false);
				}

				for (Character c : ordinary) {
					tokenizer.ordinaryChar(c);
				}
				for (Character c : wordchars) {
					tokenizer.wordChars(c, c);
				}
				for (Character c : quoteChars) {
					tokenizer.quoteChar(c);
				}
				tokenizer.eolIsSignificant(true);

			}

			@Override
			public Object read() throws IOException {
				Object result = ProgramStreamIterator.NULL;
				int brackets = 0;
				boolean lastWasNL = false;
				boolean openedBracket = false;

				try {
					if (!begun && (flags & BEGIN) != 0) {
						interpreter.begin();
						begun = true;
					}
					while (tokenizer.nextToken() != TT_EOF) {
							switch (tokenizer.ttype) {
							case TT_NUMBER:
								if(! openedBracket)
									interpreter.openBracket();
								interpreter.token(tokenizer.nval);
								break;
							case TT_WORD:
								if(! openedBracket)
									interpreter.openBracket();
								interpreter.token(convert(tokenizer.sval));
								break;
							case TT_EOL:
								if(! lastWasNL)
									result = interpreter.closeBracket();
								lastWasNL = true;
								openedBracket = false;
								break;
							default:
								interpreter.special((char) tokenizer.ttype);

							}
					}
					if (brackets > 0)
						throw new SyntaxException("Too many closing brackets");
					if (tokenizer.ttype == TT_EOF && (flags & END) != 0)
						interpreter.end();
				} catch (SyntaxException se) {
					throw new IOException(
							"Exception occured while interpreting line "
									+ tokenizer.lineno(), se);
				}
				return result;
			}

			@Override
			public void close() throws IOException {
				in.close();
			}

		};
	}

	public void ordinaryChar(char c) {
		ordinary.add(c);
	}

	public void wordChar(char c) {
		wordchars.add(c);
	}

	public void quoteChar(char c) {
		quoteChars.add(c);
	}

	public void addConverter(String pattern, Lambda<String, ?> converter) {
		converters.put(pattern, converter);
	}

	/**
	 * convenience method to parse the whole stream
	 * 
	 * @param in
	 * @return
	 * @throws SyntaxException
	 * @throws IOException
	 */
	public Object parse(Reader in) throws SyntaxException, IOException {
		ProgramStreamIterator streamIt = new ProgramStreamIterator(stream(in));
		Object result = null;
		while (streamIt.hasNext()) {
			result = streamIt.next();
		}
		return result;
	}

	protected Object convert(String sval) {
		for (String pattern : converters.keySet()) {
			if (sval.matches(pattern))
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

	/**
	 * @param flags
	 *            the flags to set
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
