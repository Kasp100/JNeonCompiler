package jneon.compiler.tokens;

import java.util.Objects;
import java.util.Optional;

import reading.Matchable;

public class Token implements Matchable<TokenType> {

	private final TokenType type;
	private final Optional<String> content;

	private Token(TokenType type, Optional<String> content) {
		this.type = Objects.requireNonNull(type);
		this.content = Objects.requireNonNull(content);
	}

	public Token(TokenType type) {
		this(type, Optional.empty());
	}

	public Token(TokenType type, String content) {
		this(type, Optional.of(content));
	}

	public TokenType getType() {
		return type;
	}

	public Optional<String> getContent() {
		return content;
	}

	@Override
	public int hashCode() {
		return Objects.hash(content, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Token other = (Token) obj;
		return Objects.equals(content, other.content) && type == other.type;
	}

	@Override
	public boolean matches(TokenType pattern) {
		return type == pattern;
	}

	@Override
	public String toString() {
		if(content.isPresent()) {
			return "[" + type + " \"" + content.get() + "\"]";
		}else {
			return "[" + type + "]";
		}
	}

}
