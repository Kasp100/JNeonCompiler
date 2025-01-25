package reading.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import reading.ReadException;

public class CharReader implements AutoCloseable {

	public static final char END_OF_FILE = 0;

	private final InputStreamReader reader;
	private boolean endOfFileReached = false;

	private final List<Character> unconsumedReadChars = new ArrayList<>();

	public CharReader(InputStreamReader reader) {
		this.reader = reader;
	}

	public char consume(int offset) throws ReadException {
		final char read = peek(offset);

		for(int i = 0; i <= offset; i++) {
			consumeNext();
		}

		return read;
	}

	public char consume() throws ReadException {
		return consume(0);
	}

	public char peek(int offset) throws ReadException {
		try {
			while(offset >= unconsumedReadChars.size()) {
				char c = readNext();

				// Change "\r" and "\r\n" to "\n"
				if(c == '\r') {
					final char next = readNext();
					if(next == '\n') {
						c = '\n';
					}else {
						c = next;
						unconsumedReadChars.add('\n');
					}
				}

				unconsumedReadChars.add(c);
			}
			return unconsumedReadChars.get(offset);
		} catch (IOException e) {
			throw new ReadException(e);
		}
	}
	
	private char readNext() throws IOException {
		if(endOfFileReached) {
			return END_OF_FILE;
		}
		final int charRead = reader.read();

		if(charRead == -1) {
			endOfFileReached = true;
			return END_OF_FILE;
		}else {
			return (char) charRead;
		}
	}

	public char peek() throws ReadException {
		return peek(0);
	}

	public boolean endOfFileReached() throws ReadException {
		return peek() == END_OF_FILE;
	}

	public boolean consumeIfMatches(char matching) throws ReadException {
		final char peek = peek();

		if(peek == matching) {
			consume();
			return true;
		}else {
			return false;
		}
	}

	public boolean consumeAllIfNext(String matchNext) throws ReadException {
		peek(matchNext.length() - 1); // Peek last char first for optimisation
		for(int i = 0; i < matchNext.length(); i++) {
			if(peek(i) != matchNext.charAt(i)) {
				return false;
			}
		}
		consume(matchNext.length() - 1);
		return true;
	}

	protected char consumeNext() {
		return unconsumedReadChars.remove(0);
	}

	@Override
	public void close() throws Exception {
		reader.close();
	}

}
