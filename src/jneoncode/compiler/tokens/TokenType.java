package jneoncode.compiler.tokens;

import java.util.Optional;

public enum TokenType {
	EOF,
	REFERENCE,
	STATIC_ACCESSOR("::");
	
	private final Optional<String> syntax;
	
	private TokenType(String syntax) {
		this.syntax = Optional.of(syntax);
	}

	private TokenType() {
		this.syntax = Optional.empty();
	}
	
	public Optional<String> getSyntax() {
		return syntax;
	}
	
	public static Optional<TokenType> match(String s) {
		for(TokenType tt : values()) {
			if(tt.getSyntax().equals(s)) {
				return Optional.of(tt);
			}
		}
		return Optional.empty();
	}
	
}
