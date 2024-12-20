package main;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import jneon.compiler.JNeonCompiler;

public class Main {

	public static void main(String[] args) {
		new Main().run(args);
	}

	public void run(String... args) {
		try {
			new JNeonCompiler(StandardCharsets.UTF_8).compile(true, convertToFiles(args));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private File[] convertToFiles(String... paths) {
		final File[] files = new File[paths.length];

		for(int i = 0; i < files.length; i++) {
			files[i] = new File(paths[i]);
		}

		return files;
	}

}
