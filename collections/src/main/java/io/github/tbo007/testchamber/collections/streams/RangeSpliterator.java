package io.github.tbo007.testchamber.collections.streams;

import java.util.Collections;
import java.util.Iterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public abstract class RangeSpliterator <T> extends Spliterators.AbstractSpliterator<T> {

    private enum Dir {
        FORWARD,
        BACKWARD
    }

    private long start;
    private final long end;
    private long fetchedUntil;

    private final long chunkSize;

    private Iterator<T> iterator;

    private final Dir  direction;

    public RangeSpliterator(long start, long end, long chunckSize) {
        super(Long.MAX_VALUE, 0);
        this.start = start;
        this.end = end;
        this.chunkSize = chunckSize -1;
        iterator = Collections.emptyIterator();
        direction = start < end ? Dir.FORWARD: Dir.BACKWARD;
    }

    protected abstract Iterator<T> nextChunck(long from, long to);


    private void adjustIterator() {
        while (!iterator.hasNext() && end != fetchedUntil) {
            // vorwärts start 0  end 100 ChunkEnd = 10 newStart = 11
            // rückwärts start 100  end 0 ChunkEnd = 90 newStart = 89
            long chunkEnd;
            if (direction == Dir.FORWARD) {
                chunkEnd = start + chunkSize;
                chunkEnd = Math.min(chunkEnd, end);
                iterator = nextChunck(start, chunkEnd);
                fetchedUntil = chunkEnd;
                start = chunkEnd + 1;
            } else {
                chunkEnd = end - chunkSize;
                chunkEnd = Math.max(chunkEnd, end);
                iterator = nextChunck(start, chunkEnd);
                fetchedUntil = chunkEnd;
                start = chunkEnd - 1;
            }
        }
    }

    @Override
    public final boolean tryAdvance(Consumer<? super T> action) {
        if (action == null) {
            throw  new NullPointerException("No Consumer given");
        }
        adjustIterator();
        if (iterator.hasNext()) {
            action.accept(iterator.next());
        }
        adjustIterator();
        return iterator.hasNext();
    }
}
