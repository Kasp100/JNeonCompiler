package jneon.compiler;

import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import jneon.compiler.tokens.Token;
import jneon.exceptions.TokenisationException;
import reading.ReadException;
import reading.impl.CharReader;

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
				tokenQueue.add(tokeniseCurrent());
			} catch (TokenisationException | ReadException e) {
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
		throw new TokenisationException("Failed to create token");
	}

}
