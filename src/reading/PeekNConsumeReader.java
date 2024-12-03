package reading;

public interface PeekNConsumeReader<ItemType extends Matchable<PatternType>, PatternType> {

	ItemType consume(int offset) throws ReadException;

	ItemType consume() throws ReadException;	

	ItemType peek(int offset) throws ReadException;

	ItemType peek() throws ReadException;

	boolean endOfFileReached();

	boolean consumeIfMatches(PatternType matching) throws ReadException;

}
