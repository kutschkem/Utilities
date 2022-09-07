package kutschke.interpreter;

import static java.io.StreamTokenizer.TT_EOF;
import static java.io.StreamTokenizer.TT_EOL;
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
import kutschke.utility.TransparentException;

public class NLParser {

	protected class ProgramStream extends GeneralInStream<Object> {
		private final Reader in;
		StreamTokenizer tokenizer;
		final int flags = getFlags();
		boolean begun = false;
		{
			tokenizer.resetSyntax();
			tokenizer.wordChars('A', 'Z');
			tokenizer.wordChars('a', 'z');
			tokenizer.whitespaceChars('\0', ' ');
			tokenizer.wordChars('0', '9');	//don't parse numbers
			if ((getFlags() & COMMENTS) != 0) {
				tokenizer.slashSlashComments(true);
				tokenizer.slashStarComments(true);
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

		protected ProgramStream(Reader in) {
			this.in = in;
			tokenizer = new StreamTokenizer(in);
		}

		@Override
		public Object read() throws IOException {
			Object result = ProgramStreamIterator.NULL;
			boolean lastWasNL = false;
			boolean openedBracket = false;

			try {
				if (!begun && (flags & BEGIN) != 0) {
					interpreter.begin();
					begun = true;
				}
				loop: while (tokenizer.nextToken() != TT_EOF) {
					switch (tokenizer.ttype) {
					case TT_NUMBER:
						throw new RuntimeException("Bug: Tokenizer should not parse numbers");
					case TT_WORD:
						lastWasNL = false;
						if (!openedBracket) {
							interpreter.openBracket();
							openedBracket = true;
						}
						interpreter.token(convert(tokenizer.sval));
						break;
					case TT_EOL:
						if (!lastWasNL)
							result = interpreter.closeBracket();
						lastWasNL = true;
						openedBracket = false;
						if (result != ProgramStreamIterator.NULL)
							break loop;
						break;
					default:
						lastWasNL = false;
						interpreter.special((char) tokenizer.ttype);

					}
				}
				if (openedBracket)
					result = interpreter.closeBracket();
				if (tokenizer.ttype == TT_EOF && (flags & END) != 0)
					interpreter.end();
			} catch (SyntaxException se) {
				IOException io = new IOException(
						"Exception occured while interpreting line "
								+ tokenizer.lineno(), se);
				io.setStackTrace(new StackTraceElement[] {}); // The stack trace is not helpful
				throw io;
			}
			return result;
		}

		@Override
		public void close() throws IOException {
			in.close();
		}
	}

	/**
	 * this Iterator differs from it's superclass in permitting null values in
	 * the Stream. Streams that are supposed to be used with this Iterator
	 * should return ProgramStreamIterator.NULL to mark their end. Try not to
	 * have any program return this as top-level return value, as it will mess
	 * up things
	 * 
	 * @author Michael
	 * 
	 */
	public static class ProgramStreamIterator extends StreamIterator<Object> {

		public static final Object NULL = new Object();

		public ProgramStreamIterator(InStream<Object> str) {
			super(str);
		}

		@Override
		public boolean hasNext() {
			try {
				buffer = inStr.read();
				checked = true;
			} catch (Exception e) {
				throw new TransparentException(e);
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
	private Map<String, Lambda<String, ?, ?>> converters = new LinkedHashMap<String, Lambda<String, ?, ?>>();

	public NLParser() {
	}

	public NLParser(int flags) {
		setFlags(flags);
	}

	public InStream<Object> stream(final Reader in) {
		return new ProgramStream(in);
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

	public void addConverter(String pattern, Lambda<String, ?, ?> converter) {
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
		try {
			while (streamIt.hasNext()) {
				result = streamIt.next();
			}
		} catch (TransparentException e) { // unbox relevant Exceptions
			if (e.getCause() instanceof SyntaxException)
				throw (SyntaxException) e.getCause();
			if (e.getCause() instanceof IOException)
				throw (IOException) e.getCause();
			throw e;
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
