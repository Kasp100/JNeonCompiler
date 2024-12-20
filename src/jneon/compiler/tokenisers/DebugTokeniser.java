package jneon.compiler.tokenisers;

import jneon.compiler.tokens.DebugToken;
import jneon.compiler.tokens.Token;
import jneon.exceptions.TokenisationException;
import reading.ReadException;
import reading.impl.CharReaderWSourceDocPos;

public class DebugTokeniser extends Tokeniser {

	private final CharReaderWSourceDocPos reader;

	public DebugTokeniser(CharReaderWSourceDocPos reader) {
		super(reader);
		this.reader = reader;
	}
	
	@Override
	protected Token tokeniseCurrent() throws ReadException, TokenisationException {
		return new DebugToken(super.tokeniseCurrent(), reader.getSourceDocPos());
	}

}
