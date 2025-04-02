package jneon.compiler.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import jneon.JNeonRootNode;
import jneon.compiler.CompileTimeConsumer;
import jneon.compiler.Import;
import jneon.compiler.PkgSign;
import jneon.compiler.TokenReader;
import jneon.compiler.tokens.Token;
import jneon.compiler.tokens.TokenType;
import jneon.exceptions.CompileTimeException;
import jneon.subnodes.TopLevelNode;
import reading.ReadException;

public class ASTBuilder {

	private static final TokenType
			PACKAGE_NAME = TokenType.REFERENCE,
			PACKAGE_SEPARATOR = TokenType.STATIC_ACCESSOR,
			IMPORT_SEPARATOR = TokenType.END_STATEMENT;

	public List<Import> build(JNeonRootNode.Builder builder, TokenReader reader) throws ReadException, CompileTimeException {

		final List<Import> imports = new ArrayList<>();

		final Optional<PkgSign> optPkgSign = parsePackageDecl(reader);
		if(optPkgSign.isPresent()) {
			parse(reader, (topLevelNode) -> {
				builder.add(optPkgSign.get(), topLevelNode);
			}, imports::add);
		}else {
			throw new CompileTimeException("Expected package declaration");
		}

		return imports;
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
	
	private void parse(TokenReader reader, CompileTimeConsumer<TopLevelNode> topNodeConsumer, Consumer<Import> importsConsumer)
			throws ReadException, CompileTimeException {

		while(!reader.endOfFileReached()) {

			parseImport(reader, importsConsumer);
			
		}

	}

	private void parseImport(TokenReader reader, Consumer<Import> importsConsumer) throws ReadException, CompileTimeException {
		if(!reader.consumeIfMatches(TokenType.IMPORT)) {
			return;
		}

		final String packageName = readPackageName(reader);

		importsConsumer.accept(new Import(packageName.toString()));
	}
	
	private String readPackageName(TokenReader reader) throws ReadException, CompileTimeException {

		final StringBuilder packageNameBuilder = new StringBuilder();

		while(!reader.endOfFileReached()) {
			{
				final Token token = reader.consume();
				if(token.getType() == PACKAGE_NAME) {
					packageNameBuilder.append(token.getContent().get());
				}else {
					throw new CompileTimeException("Package name expected");
				}
			}
			{
				final Token token = reader.consume();
				if(token.getType() == PACKAGE_SEPARATOR) {
					packageNameBuilder.append(PACKAGE_SEPARATOR.getSyntax().get());
				}else if(token.getType() == IMPORT_SEPARATOR) {
					break;
				}else {
					throw new CompileTimeException(
							"`" + PACKAGE_SEPARATOR.getSyntax().get() + "` or `" +
							IMPORT_SEPARATOR.getSyntax().get() + "` expected");
				}
			}
		}

		return packageNameBuilder.toString();
	}

}
