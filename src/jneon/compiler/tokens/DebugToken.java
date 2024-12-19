package jneon.compiler.tokens;

import reading.impl.SourceDocPos;

public class DebugToken extends Token {

	private final SourceDocPos pos;

	public DebugToken(Token token, SourceDocPos pos) {
		super(token);
		this.pos = pos;
	}

	public SourceDocPos getPos() {
		return pos;
	}

}
