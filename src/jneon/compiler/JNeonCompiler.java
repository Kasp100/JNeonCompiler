package jneon.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import jneon.JNeonTopNode;
import jneon.compiler.tokenisers.Tokeniser;
import reading.impl.CharReaderWSourceDocPos;

public class JNeonCompiler {

	private final Charset charset;

	public JNeonCompiler(Charset charset) {
		this.charset = charset;
	}

	public JNeonTopNode compile(File... sourceFiles) throws FileNotFoundException, IOException {
		final JNeonTopNode.Builder builder = new JNeonTopNode.Builder();
		for(File file : sourceFiles) {
			tokeniseAndBuildAST(builder, file);
		}
		resolveReferences();
		return builder.build();
	}

	private void tokeniseAndBuildAST(JNeonTopNode.Builder builder, File file) throws FileNotFoundException, IOException {
		try(final FileReader reader = new FileReader(file, charset)) {
			tokeniseAndBuildAST(builder, reader, file.getCanonicalPath());
		}
	}
	
	private void tokeniseAndBuildAST(JNeonTopNode.Builder builder, InputStreamReader reader, String fileName) {
		final Tokeniser tokeniser = new Tokeniser(new CharReaderWSourceDocPos(reader, fileName));
		buildAST(builder, tokeniser.getTokenReader());
	}

	private void buildAST(JNeonTopNode.Builder builder, TokenReader tr) {
		
	}

	private void resolveReferences() {
		
	}

}
