package jneon.compiler;

import java.util.List;
import java.util.Objects;

import jneon.compiler.tokens.Token;
import jneon.compiler.tokens.TokenType;
import jneon.exceptions.CompileTimeException;

public class PkgSign {

	private final List<Token> id;

	public PkgSign(List<Token> id) throws CompileTimeException {
		this.id = List.copyOf(id);
		checkValidity();
	}
	
	private void checkValidity() throws CompileTimeException {
		for (int i = 0; i < id.size(); i++) {
			if(i % 2 == 1) {
				if(id.get(i).getType() != TokenType.REFERENCE) {
					throw new CompileTimeException("Expected package name");
				}
			}else {
				if(id.get(i).getType() != TokenType.STATIC_ACCESSOR) {
					throw new CompileTimeException("Expected " + TokenType.STATIC_ACCESSOR);
				}
			}
		}
	}

	@Override
	public String toString() {
		final StringBuilder not = new StringBuilder();

		for(Token t : id) {
			if(t.getType() == TokenType.STATIC_ACCESSOR) {
				not.append(TokenType.STATIC_ACCESSOR.getSyntax().get());
			}else {
				not.append(t.getContent().get());				
			}
		}

		return not.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PkgSign other = (PkgSign) obj;
		return Objects.equals(id, other.id);
	}

}
