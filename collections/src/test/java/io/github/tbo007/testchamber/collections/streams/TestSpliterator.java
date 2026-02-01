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
    void testForward() {

        SortedMap<Long, Integer> svn = getSvn();
        List<Integer> revs = buildSpliterator(svn, 0, 3000);
        assertEquals(new ArrayList<>( svn.values()),revs);
    }
    @Test
    void testBackward() {

        SortedMap<Long, Integer> svn = getSvn();
        List<Integer> revs = buildSpliterator(svn, 3000, 0);
        ArrayList<Integer> expected = new ArrayList<>(svn.values());
        expected.sort(Comparator.reverseOrder());
        assertEquals(expected,revs);
    }

    private static SortedMap<Long, Integer> getSvn() {
        SortedMap<Long,Integer> svn = new TreeMap<>();
        svn.put(3L,3);
        svn.put(999L,999);
        svn.put(2000L,2000);
        svn.put(3000L,3000);
        return svn;
    }

    public List<Integer> buildSpliterator (SortedMap<Long,Integer> input, long from, long to) {
        Spliterator<Integer> spi= new RangeSpliterator<>(from, to, 1000) {
            @Override
            protected Iterator<Integer> nextChunck(long from, long to) {
                SortedMap<Long, Integer> range;
                if (from > to) {
                    range  = input.subMap(to, from + 1);
                } else {
                    range = input.subMap(from, to + 1);
                }
                List<Integer> values = new ArrayList<>(range.values());
                System.out.println("f:" + from + " t: "+ to + " size: " +values.size());
                if(from > to) {
                    Collections.sort(values, Comparator.reverseOrder());
                }
                return values.iterator();
            }
        };
        List<Integer> revs = new ArrayList<>();
        while (spi.tryAdvance(revs::add));
        return revs;
    }
}
