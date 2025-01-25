package jneon.compiler.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jneon.JNeonRootNode;
import jneon.compiler.CompileTimeConsumer;
import jneon.compiler.PkgSign;
import jneon.compiler.TokenReader;
import jneon.compiler.tokens.Token;
import jneon.compiler.tokens.TokenType;
import jneon.exceptions.CompileTimeException;
import jneon.subnodes.TopLevelNode;
import reading.ReadException;

public class ASTBuilder {

	public void build(JNeonRootNode.Builder builder, TokenReader reader) throws ReadException, CompileTimeException {

		final Optional<PkgSign> optPkgSign = parsePackageDecl(reader);
		if(optPkgSign.isPresent()) {
			parse(reader, (topLevelNode) -> {
				builder.add(optPkgSign.get(), topLevelNode);
			});
		}else {
			throw new CompileTimeException("Expected package declaration");
		}

	}
	
	private Optional<PkgSign> parsePackageDecl(TokenReader reader) throws ReadException, CompileTimeException {

		if(!reader.consumeIfMatches(TokenType.PACKAGE_DECL)) {
			return Optional.empty();
		}

		final List<Token> pkgSignTokens = new ArrayList<>();
		while(!reader.endOfFileReached() && !reader.consumeIfMatches(TokenType.END_STATEMENT)) {
			pkgSignTokens.add(reader.consume());
		}
		
		return Optional.of(new PkgSign(pkgSignTokens));

	}
	
	private void parse(TokenReader reader, CompileTimeConsumer<TopLevelNode> topNodeConsumer) throws CompileTimeException {
		
	}

}
