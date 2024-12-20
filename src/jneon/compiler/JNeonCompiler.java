package jneon.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import jneon.JNeonTopNode;
import jneon.compiler.tokenisers.DebugTokeniser;
import jneon.compiler.tokenisers.Tokeniser;
import reading.ReadException;
import reading.impl.CharReader;
import reading.impl.CharReaderWSourceDocPos;

public class JNeonCompiler {

	private final JNeonTopNode topNode = new JNeonTopNode();
	private final Charset charset;

	public JNeonCompiler(Charset charset) {
		this.charset = charset;
	}

	public JNeonTopNode getTopNode() {
		return topNode;
	}

	public void compile(boolean includeSourceCodePos, File... sourceFiles) throws FileNotFoundException, IOException {
		for(File file : sourceFiles) {
			tokeniseAndBuildAST(file, includeSourceCodePos);
		}
		resolveReferences();
	}

	private void tokeniseAndBuildAST(File file, boolean includeSourceCodePos) throws FileNotFoundException, IOException {
		try(final FileReader reader = new FileReader(file, charset)) {
			tokeniseAndBuildAST(reader, file.getCanonicalPath(), includeSourceCodePos);
		}
	}
	
	private void tokeniseAndBuildAST(InputStreamReader reader, String fileName, boolean includeSourceCodePos) {
		final Tokeniser tokeniser;

		if(includeSourceCodePos) {
			tokeniser = new DebugTokeniser(new CharReaderWSourceDocPos(reader, fileName));
		}else {
			tokeniser = new Tokeniser(new CharReader(reader));
		}
		
		buildAST(tokeniser.getTokenReader());
	}

	private void buildAST(TokenReader tr) {
		while(!tr.endOfFileReached()) {
			try {
				System.out.println(tr.consume());
			} catch (ReadException e) {
				e.printStackTrace();
			}
		}
	}

	private void resolveReferences() {
		
	}

}
