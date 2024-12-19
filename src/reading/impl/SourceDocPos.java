package reading.impl;

import java.util.Objects;

public class SourceDocPos {

	private final String file;
	private final int lineIdx, colIdx;

	public SourceDocPos(String file, int lineIdx, int colIdx) {
		this.file = file;
		this.lineIdx = lineIdx;
		this.colIdx = colIdx;
	}

	public String getFile() {
		return file;
	}

	public int getLineIdx() {
		return lineIdx;
	}

	public int getColIdx() {
		return colIdx;
	}

	@Override
	public int hashCode() {
		return Objects.hash(colIdx, file, lineIdx);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SourceDocPos other = (SourceDocPos) obj;
		return colIdx == other.colIdx && Objects.equals(file, other.file) && lineIdx == other.lineIdx;
	}

	@Override
	public String toString() {
		return file + ":" + (lineIdx+1) + ":" + (colIdx+1);
	}

}
