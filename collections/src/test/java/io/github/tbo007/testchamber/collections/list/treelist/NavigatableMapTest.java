package io.github.tbo007.testchamber.collections.list.treelist;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class NavigatableMapTest {

    @Test
    void headTailTest () {
        Map<Integer, String> m = Map.of(7, "", 2, "", 3, "", 4, "", 5, "");
        NavigableMap<Integer, String> navMap = new TreeMap<>(m);
        System.out.println(navMap);
        System.out.println("headMap " +navMap.headMap(3,true));
        System.out.println("tailMap " +navMap.tailMap(3,true));
        System.out.println(navMap.tailMap(3,true).descendingMap());
    }

}
