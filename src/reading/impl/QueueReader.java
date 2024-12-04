package reading.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import reading.Matchable;
import reading.PeekNConsumeReader;
import reading.ReadException;

public class QueueReader<ItemType extends Matchable<PatternType>, PatternType> implements PeekNConsumeReader<ItemType, PatternType> {

	private final BlockingQueue<ItemType> itemQueue;
	private final ItemType endOfFileItem;

	private final List<ItemType> unconsumedPolledItems = new LinkedList<>();

	private boolean endOfFile = false;

	public QueueReader(BlockingQueue<ItemType> itemQueue, ItemType endOfFileItem) {
		this.itemQueue = itemQueue;
		this.endOfFileItem = endOfFileItem;
	}

	public void triggerEndOfFile() {
		endOfFile = true;
	}

	@Override
	public ItemType consume(int offset) throws ReadException {
		final ItemType read = peek(offset);

		for(int i = 0; i <= offset; i++) {
			unconsumedPolledItems.remove(0);
		}

		return read;
	}

	@Override
	public ItemType consume() throws ReadException {
		return consume(0);
	}

	@Override
	public ItemType peek(int offset) throws ReadException {
		while(unconsumedPolledItems.size() <= offset) {
			poll();
		}
		return unconsumedPolledItems.get(offset);
	}

	@Override
	public ItemType peek() throws ReadException {
		return peek(0);
	}

	@Override
	public boolean endOfFileReached() {
		return endOfFile;
	}

	@Override
	public boolean consumeIfMatches(PatternType matching) throws ReadException {
		if(peek().matches(matching)) {
			consume();
			return true;
		}else {
			return false;
		}
	}

	private void poll() {
		ItemType polled = itemQueue.poll();
		while(polled == null) {
			if(endOfFileReached()) {
				unconsumedPolledItems.add(endOfFileItem);
				return;
			}else {
				polled = itemQueue.poll();
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		unconsumedPolledItems.add(polled);
	}

}
