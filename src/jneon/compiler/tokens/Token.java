package jneon.compiler.tokens;

import java.util.Objects;
import java.util.Optional;

import reading.Matchable;
import reading.impl.SourceDocPos;

public class Token implements Matchable<TokenType> {

	private final TokenType type;
	private final Optional<String> content;

	private final SourceDocPos pos;

	public Token(TokenType type, Optional<String> content, SourceDocPos pos) {
		this.type = Objects.requireNonNull(type);
		this.content = Objects.requireNonNull(content);
		this.pos = Objects.requireNonNull(pos);
	}

	public Token(Token other) {
		this(other.getType(), other.getContent(), other.getPos());
	}

	public Token(TokenType type, SourceDocPos pos) {
		this(type, Optional.empty(), pos);
	}

	public Token(TokenType type, String content, SourceDocPos pos) {
		this(type, Optional.of(content), pos);
	}

	public TokenType getType() {
		return type;
	}

	public Optional<String> getContent() {
		return content;
	}

	public SourceDocPos getPos() {
		return pos;
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
			return "[" + type + " \"" + content.get() + "\" (" + pos + ")]";
		}else {
			return "[" + type + " (" + pos + ")]";
		}
	}

}
