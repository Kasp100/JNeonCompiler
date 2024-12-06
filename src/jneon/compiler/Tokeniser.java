package jneon.compiler;

import java.io.InputStreamReader;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import jneon.compiler.tokens.Token;
import jneon.compiler.tokens.TokenType;
import jneon.exceptions.CompileTimeException;
import jneon.exceptions.TokenisationException;
import reading.ReadException;
import reading.impl.CharReader;
import reading.impl.MatchableChar;

public class Tokeniser {

	private static final int TOKEN_QUEUE_CAPACITY = 127;

	private final CharReader reader;
	private final BlockingQueue<Token> tokenQueue = new ArrayBlockingQueue<>(TOKEN_QUEUE_CAPACITY);
	private final TokenReader tokenReader = new TokenReader(tokenQueue);

	public Tokeniser(InputStreamReader reader) {
		this.reader = new CharReader(reader);
		new Thread(this::tokenise, "tokenising").start();
	}

	private void tokenise() {
		while(!reader.endOfFileReached()) {
			try {
				if(Character.isWhitespace(reader.peek().getChar())) {
					reader.consume();
					continue;
				}
				if(!skipComment()) {
					tokenQueue.add(tokeniseCurrent());
				}
			} catch (Exception e) {
				System.err.println("Tokenisation failed!");
				e.printStackTrace();
				break;
			}
		}

		tokenReader.triggerEndOfFile();
	}

	public TokenReader getTokenReader() {
		return tokenReader;
	}

	private Token tokeniseCurrent() throws ReadException, TokenisationException {
		{
			final Optional<Token> parsedBracket = parseBracket();
			if(parsedBracket.isPresent()) {
				return parsedBracket.get();
			}
		}

		{
			final Optional<Token> parsedStringLit = parseString();
			if(parsedStringLit.isPresent()) {
				return parsedStringLit.get();
			}
		}

		if(reader.consumeIfMatches(';')) {
			return new Token(TokenType.END_STATEMENT);
		}

		if(reader.consumeIfMatches(',')) {
			return new Token(TokenType.COMMA);
		}

		if(reader.consumeAllIfNext("mut:")) {
			return new Token(TokenType.MUTABLE_REF);
		}

		{
			final Optional<Token> parsedWord = parseWord();
			if(parsedWord.isPresent()) {
				return parsedWord.get();
			}
		}

		if(reader.peek(0).matches(':') && reader.peek(1).matches(':')) {
			reader.consume(1);
			return new Token(TokenType.STATIC_ACCESSOR);
		}

		throw new TokenisationException("Failed to create token (last char read: " + reader.peek().getChar() + ")");
	}

	private Optional<Token> parseString() throws ReadException, CompileTimeException {
		if(!reader.consumeIfMatches('"')) {
			return Optional.empty();
		}

		final StringBuilder parsedString = new StringBuilder();

		boolean escapeSequence = false;

		MatchableChar c = reader.consume();

		while(escapeSequence || !c.matches('"')) {

			if(escapeSequence) {
				escapeSequence = false;
				parsedString.append(parseEscapeSequence(c.getChar()));
			}else if(c.matches('\\')) {
				escapeSequence = true;
			}else if(c.isEndOfFile()) {
				throw new CompileTimeException("End of file reading string");
			}else {
				parsedString.append(c.getChar());
			}

			c = reader.consume();
		}

		return Optional.of(new Token(TokenType.STRING_LITERAL, parsedString.toString()));
	}
	
	private char parseEscapeSequence(char c) throws CompileTimeException {
		if(c == 'b') {
			return '\b';
		}else if(c == 't') {
			return '\t';
		}else if(c == 'n') {
			return '\n';
		}else if(c == 'f') {
			return '\f';
		}else if(c == 'r') {
			return '\r';
		}else if(c == '"') {
			return '\"';
		}else if(c == '\'') {
			return '\'';
		}else if(c == '\\') {
			return '\\';
		}else {
			throw new CompileTimeException("Invalid escape sequence: \\" + c + " (valid ones are \\b  \\t  \\n  \\f  \\r  \\\"  \\'  \\\\)");
		}
	}

	private Optional<Token> parseBracket() throws ReadException {
		if(reader.consumeIfMatches('{')) {
			return Optional.of(new Token(TokenType.CURLY_OPEN));
		}else if(reader.consumeIfMatches('}')) {
			return Optional.of(new Token(TokenType.CURLY_CLOSE));
		}else if(reader.consumeIfMatches('<')) {
			return Optional.of(new Token(TokenType.SMALLER_THAN));
		}else if(reader.consumeIfMatches('>')) {
			return Optional.of(new Token(TokenType.GREATER_THAN));
		}else if(reader.consumeIfMatches('(')) {
			return Optional.of(new Token(TokenType.ROUND_OPEN));
		}else if(reader.consumeIfMatches(')')) {
			return Optional.of(new Token(TokenType.ROUND_CLOSE));
		}else {
			return Optional.empty();
		}
	}

	/** Parse a keyword or a reference. */
	private Optional<Token> parseWord() throws ReadException, CompileTimeException {
		final StringBuilder word = new StringBuilder();
		boolean expectWord = false;

		if(reader.consumeIfMatches('#')) {
			word.append('#');
			expectWord = true;
		}

		if(readWordChar(word, true)) {
			while(readWordChar(word, false)); // Read all characters in the word
			return Optional.of(parseKeywordOrReference(word.toString()));
		}else {
			return failOrEmpty(expectWord, "Expected word");
		}
	}
	
	private boolean readWordChar(StringBuilder word, boolean start) throws ReadException {
		final MatchableChar c = reader.peek();

		if(c.isEndOfFile()) {
			throw new CompileTimeException("Unexpected end of file (reading word)");
		}else if((start && Character.isJavaIdentifierStart(c.getChar())) ||
				(!start && Character.isJavaIdentifierPart(c.getChar()))) {
			word.append(c.getChar());
			reader.consume();
			return true;
		}else {
			return false;
		}
	}

	private Token parseKeywordOrReference(String word) {
		final Optional<TokenType> tt = TokenType.match(word);
		if(tt.isPresent()) {
			return new Token(tt.get());
		}else {
			return new Token(TokenType.REFERENCE, word);
		}
	}

	private <T> Optional<T> failOrEmpty(boolean expect, String failureMessage) throws CompileTimeException {
		if(expect) {
			throw new CompileTimeException(failureMessage);
		}else {
			return Optional.empty();
		}
	}
	
	private boolean skipComment() throws ReadException {
		if(reader.consumeAllIfNext("/*")) {
			while(!reader.consumeAllIfNext("*/"))// Read until comment ends
			{
				reader.consume();
			}
			return true;
		}else if(reader.consumeAllIfNext("//")) {
			while(!reader.consumeIfMatches('\n'))// Read until next line
			{
				reader.consume();
			}
			return true;
		}
		return false;
	}

}
