package reading.impl;

import java.util.Objects;

import reading.Matchable;

public class MatchableChar implements Matchable<MatchableChar> {

	public static final MatchableChar END_OF_FILE = new MatchableChar();

	private final char c;
	private final boolean endOfFile;

	public MatchableChar(char c) {
		this.c = c;
		this.endOfFile = false;
	}

	private MatchableChar() {
		this.c = 0;
		endOfFile = true;
	}

	public char getChar() {
		return c;
	}

	public boolean isEndOfFile() {
		return endOfFile;
	}

	@Override
	public boolean matches(MatchableChar pattern) {
		return equals(pattern);
	}

	public boolean matches(char pattern) {
		return matches(new MatchableChar(pattern));
	}

	@Override
	public int hashCode() {
		return Objects.hash(c, endOfFile);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MatchableChar other = (MatchableChar) obj;
		return c == other.c && endOfFile == other.endOfFile;
	}

}
