package reading.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import reading.PeekNConsumeReader;
import reading.ReadException;

public class CharReader implements PeekNConsumeReader<MatchableChar, MatchableChar> {

	private static final MatchableChar NEWLINE = new MatchableChar('\n');

	private final InputStreamReader reader;

	private final List<MatchableChar> unconsumedReadChars = new LinkedList<>();

	public CharReader(InputStreamReader reader) {
		this.reader = reader;
	}

	@Override
	public MatchableChar consume(int offset) throws ReadException {
		final MatchableChar read = peek(offset);

		for(int i = 0; i <= offset; i++) {
			consumeNext();
		}

		return read;
	}

	@Override
	public MatchableChar consume() throws ReadException {
		return consume(0);
	}

	@Override
	public MatchableChar peek(int offset) throws ReadException {
		try {
			while(offset >= unconsumedReadChars.size()) {
				MatchableChar c = readNext();

				if(c.matches('\r')) {
					final MatchableChar next = readNext();
					if(next.matches(NEWLINE)) {
						c = NEWLINE;
					}else {
						c = next;
						unconsumedReadChars.add(NEWLINE);
					}
				}

				unconsumedReadChars.add(c);
			}
			return unconsumedReadChars.get(offset);
		} catch (IOException e) {
			throw new ReadException(e);
		}
	}
	
	private MatchableChar readNext() throws IOException {
		final int charRead = reader.read();

		if(charRead == -1) {
			return MatchableChar.END_OF_FILE;
		}else {
			return new MatchableChar((char) charRead);
		}
	}

	@Override
	public MatchableChar peek() throws ReadException {
		return peek(0);
	}
	
	@Override
	public boolean endOfFileReached() throws ReadException {
		return peek().isEndOfFile();
	}

	@Override
	public boolean consumeIfMatches(MatchableChar matching) throws ReadException {
		final MatchableChar peek = peek();

		if(peek.matches(matching)) {
			consume();
			return true;
		}else {
			return false;
		}
	}

	public boolean consumeIfMatches(char matching) throws ReadException {
		return consumeIfMatches(new MatchableChar(matching));
	}

	public boolean consumeAllIfNext(String matchNext) throws ReadException {
		peek(matchNext.length() - 1); // Peek last char first for optimisation
		for(int i = 0; i < matchNext.length(); i++) {
			if(!peek(i).matches(matchNext.charAt(i))) {
				return false;
			}
		}
		consume(matchNext.length() - 1);
		return true;
	}

	protected MatchableChar consumeNext() {
		return unconsumedReadChars.remove(0);
	}

}
