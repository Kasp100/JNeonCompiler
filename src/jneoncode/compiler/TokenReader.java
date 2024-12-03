package jneoncode.compiler;

import java.util.concurrent.BlockingQueue;

import jneoncode.compiler.tokens.Token;
import jneoncode.compiler.tokens.TokenType;
import reading.impl.QueueReader;

public class TokenReader extends QueueReader<Token, TokenType> {

	public TokenReader(BlockingQueue<Token> itemQueue) {
		super(itemQueue);
	}

}
