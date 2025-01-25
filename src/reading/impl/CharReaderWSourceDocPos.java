package reading.impl;

import java.io.InputStreamReader;

public class CharReaderWSourceDocPos extends CharReader {

	private int lineIdx = 0, colIdx = 0;
	private final String file;

	public CharReaderWSourceDocPos(InputStreamReader reader, String file) {
		super(reader);
		this.file = file;
	}

	@Override
	protected char consumeNext() {
		final char charRead = super.consumeNext();

		if(charRead == '\n') {
			lineIdx++;
			colIdx = 0;
		}else {
			colIdx++;
		}

		return charRead;
	}
	
	public SourceDocPos getSourceDocPos() {
		return new SourceDocPos(file, lineIdx, colIdx);
	}

}
