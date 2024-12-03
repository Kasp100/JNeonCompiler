package reading.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import reading.PeekNConsumeReader;
import reading.ReadException;

public class CharReader implements PeekNConsumeReader<MatchableChar, MatchableChar> {

	private final InputStreamReader reader;

	private List<MatchableChar> unconsumedReadChars = new LinkedList<>();

	private boolean doneReading = false;

	public CharReader(InputStreamReader reader) {
		this.reader = reader;
	}

	@Override
	public MatchableChar consume(int offset) throws ReadException {
		final MatchableChar read = peek(offset);

		for(int i = 0; i <= offset; i++) {
			unconsumedReadChars.remove(0);
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
				final int charRead = reader.read();

				if(charRead == -1) {
					unconsumedReadChars.add(MatchableChar.END_OF_FILE);
					
				}else {
					unconsumedReadChars.add(new MatchableChar((char) charRead));
				}
			}
			return unconsumedReadChars.get(offset);
		} catch (IOException e) {
			throw new ReadException(e);
		}
	}

	@Override
	public MatchableChar peek() throws ReadException {
		return peek(0);
	}
	
	@Override
	public boolean endOfFileReached() {
		return unconsumedReadChars.isEmpty() && doneReading;
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

}
