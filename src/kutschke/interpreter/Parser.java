package kutschke.interpreter;

import static java.io.StreamTokenizer.TT_EOF;
import static java.io.StreamTokenizer.TT_NUMBER;
import static java.io.StreamTokenizer.TT_WORD;

import java.io.IOException;
import java.io.Reader;
import kutschke.generalStreams.InStream;
import kutschke.higherClass.ReflectiveFun;

public class Parser extends NLParser {

	protected class ProgramStream extends NLParser.ProgramStream {
		final char openBracket = getBracketOpen();
		final char closeBracket = getBracketClose();

		protected ProgramStream(Reader in) {
			super(in);
		}

		@Override
		public Object read() throws IOException {
			Object result = ProgramStreamIterator.NULL;
			int brackets = 0;

			try {
				if ((flags & BEGIN) != 0)
					interpreter.begin();
				while (tokenizer.nextToken() != TT_EOF) {
					if (tokenizer.ttype == openBracket) {
						interpreter.openBracket();
						brackets++;
					} else if (tokenizer.ttype == closeBracket) {
						brackets--;
						if (brackets < 0)
							throw new SyntaxException(
									"Too many closing brackets");
						result = interpreter.closeBracket();
						if (brackets == 0)
							break;
					} else
						switch (tokenizer.ttype) {
						case TT_NUMBER:
							throw new RuntimeException(
									"Bug: Tokenizer should not parse Numbers");
						case TT_WORD:
							interpreter.token(convert(tokenizer.sval));
							break;
						default:
							interpreter.special((char) tokenizer.ttype);

						}
				}
				if (brackets > 0)
					throw new SyntaxException("Missing closing brackets");
				if ((flags & END) != 0)
					interpreter.end();
			} catch (SyntaxException se) {
				IOException io = new IOException(
						"Exception occured while interpreting line "
								+ tokenizer.lineno(), se);
				io.setStackTrace(new StackTraceElement[] {});
				throw io;
			}
			return result;
		}

	}

	private char bracketOpen = '(';
	private char bracketClose = ')';

	public Parser() {
		super();
	}

	public Parser(int flags) {
		super(flags);
	}

	/**
	 * @param bracketOpen
	 *            the bracketOpen to set
	 */
	public void setBracketOpen(char bracketOpen) {
		this.bracketOpen = bracketOpen;
	}

	protected char getBracketOpen() {
		return bracketOpen;
	}

	/**
	 * @param bracketClose
	 *            the bracketClose to set
	 */
	public void setBracketClose(char bracketClose) {
		this.bracketClose = bracketClose;
	}

	protected char getBracketClose() {
		return bracketClose;
	}

	/**
	 * returns a parsing stream that returns every top-level result
	 * 
	 * @param in
	 * @return
	 */
	public InStream<Object> stream(final Reader in) {
		return new ProgramStream(in);
	}

	public static Parser standardParser() {
		Parser parser = new Parser();
		// wordchars for proper parsing of Classnames, numbers etc.
		parser.wordChar('_');
		parser.wordChar('.');
		parser.wordChar('-');
		parser.wordChar('[');
		parser.wordChar(';');

		try {
			parser.addConverter("[+\\-]?\\d+(\\.\\d+)?",
					new ReflectiveFun<Number>("parseDouble", Double.class,
							String.class).singleParameterAdapter(String.class));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return parser;
	}

}
