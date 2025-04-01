package main;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import jneon.compiler.JNeonCompiler;

public class Main {

	public static void main(String[] args) {
		new Main().run(args);
	}

	private static final Charset CHARSET = StandardCharsets.UTF_8;

	public void run(String... args) {
		new JNeonCompiler(CHARSET).compile((e) -> {
			e.printStackTrace();
		}, convertToFiles(args));
	}
	
	private File[] convertToFiles(String... paths) {
		final File[] files = new File[paths.length];

		for(int i = 0; i < files.length; i++) {
			files[i] = new File(paths[i]);
		}

		return files;
	}

}
