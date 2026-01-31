package io.github.tbo007.testchamber.collections.streams;

import org.junit.jupiter.api.Test;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestSpliterator {


    /**
     *  0 --> 3000
     *  1. 0-999
     *  2. 1000 - 1999
     *  3  2000 - 2999
     *  4. 3000 - 3000
     */
    @Test
    void testSpliterator() {

        SortedMap<Long,Integer> svn = new TreeMap<>();
        svn.put(3L,3);
        svn.put(999L,999);
        svn.put(2000L,2000);
        svn.put(3000L,3000);

        Spliterator<Integer> spi= new RangeSpliterator<>(0, 3000, 1000) {
            @Override
            protected Iterator<Integer> nextChunck(long from, long to) {
                System.out.println("f:" + from + " t: "+ to);
                SortedMap<Long, Integer> range = svn.subMap(from, to + 1);
                return range.values().iterator();
            }
        };
        List<Integer> revs = new ArrayList<>();
        while (spi.tryAdvance(revs::add));
        assertEquals(new ArrayList<>( svn.values()),revs);
    }
}
