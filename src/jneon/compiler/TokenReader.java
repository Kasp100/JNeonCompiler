package jneon.compiler;

import java.util.concurrent.BlockingQueue;

import jneon.compiler.tokens.Token;
import jneon.compiler.tokens.TokenType;
import reading.impl.QueueReader;

public class TokenReader extends QueueReader<Token, TokenType> {

	public TokenReader(BlockingQueue<Token> itemQueue) {
		super(itemQueue, new Token(TokenType.EOF));
	}

}
