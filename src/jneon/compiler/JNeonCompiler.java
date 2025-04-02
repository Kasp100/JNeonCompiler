package jneon.compiler;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.function.Consumer;

import jneon.JNeonRootNode;
import jneon.compiler.ast.ASTBuilder;
import jneon.compiler.tokenisers.Tokeniser;
import jneon.exceptions.CompileTimeException;
import reading.ReadException;
import reading.impl.CharReaderWSourceDocPos;

public class JNeonCompiler {

	private final Charset charset;

	public JNeonCompiler(Charset charset) {
		this.charset = charset;
	}

	public JNeonRootNode compile(Consumer<Exception> exceptionHandler, File... sourceFiles) {
		final JNeonRootNode.Builder builder = new JNeonRootNode.Builder();
		for(File file : sourceFiles) {
			tokeniseAndBuildAST(exceptionHandler, builder, file);
		}
		resolveReferences();
		return builder.build();
	}

	private void tokeniseAndBuildAST(
			Consumer<Exception> exceptionHandler,
			JNeonRootNode.Builder builder,
			File file)
	{
		try {
			tokeniseAndBuildAST(
					exceptionHandler,
					builder,
					() -> new FileReader(file, charset),
					file.getCanonicalPath());
		} catch (IOException e) {
			exceptionHandler.accept(e);
		}
	}

	private void tokeniseAndBuildAST(
			Consumer<Exception> exceptionHandler,
			JNeonRootNode.Builder builder,
			ResourceSupplier<InputStreamReader, IOException> readerSupplier,
			String fileName)
	{
		final Tokeniser tokeniser = new Tokeniser(exceptionHandler,
				() -> new CharReaderWSourceDocPos(readerSupplier.create(), fileName));
		final TokenReader reader = tokeniser.getTokenReader();
		try {
			buildAST(builder, reader);
		} catch (ReadException | CompileTimeException e1) {
			try {
				final String msg = e1.getMessage() + " at " + reader.peek().getPos().toString();
				exceptionHandler.accept(new CompileTimeException(msg));
			} catch (ReadException e2) {
				exceptionHandler.accept(new CompileTimeException(e1));
			}
		}
	}

	private void buildAST(JNeonRootNode.Builder builder, TokenReader tr) throws ReadException, CompileTimeException {
		 new ASTBuilder().build(builder, tr);
	}

	private void resolveReferences() {
		
	}

}
