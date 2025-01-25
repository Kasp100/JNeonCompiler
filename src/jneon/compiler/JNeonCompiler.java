package jneon.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import jneon.JNeonRootNode;
import jneon.compiler.tokenisers.Tokeniser;
import reading.impl.CharReaderWSourceDocPos;

public class JNeonCompiler {

	private final Charset charset;

	public JNeonCompiler(Charset charset) {
		this.charset = charset;
	}

	public JNeonRootNode compile(File... sourceFiles) throws FileNotFoundException, IOException {
		final JNeonRootNode.Builder builder = new JNeonRootNode.Builder();
		for(File file : sourceFiles) {
			tokeniseAndBuildAST(builder, file);
		}
		resolveReferences();
		return builder.build();
	}

	private void tokeniseAndBuildAST(JNeonRootNode.Builder builder, File file) throws FileNotFoundException, IOException {
		tokeniseAndBuildAST(builder, () -> {
			return new FileReader(file, charset);
		}, file.getCanonicalPath());
	}

	private void tokeniseAndBuildAST(JNeonRootNode.Builder builder, ResourcePromise<InputStreamReader, IOException> reader, String fileName) {
		final Tokeniser tokeniser = new Tokeniser(() -> new CharReaderWSourceDocPos(reader.create(), fileName));
		buildAST(builder, tokeniser.getTokenReader());
	}

	private void buildAST(JNeonRootNode.Builder builder, TokenReader tr) {
		
	}

	private void resolveReferences() {
		
	}

}
