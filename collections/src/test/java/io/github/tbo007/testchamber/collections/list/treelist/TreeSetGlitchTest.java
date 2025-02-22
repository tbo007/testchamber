package io.github.tbo007.testchamber.collections.list.treelist;

import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.*;

public class TreeSetGlitchTest {

    private  class Compparator implements Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2) >= 0 ? 1: -1  ;
        }
    }


    @Test
    void  testGlitchComp(){
        List<Integer> testList = getTestList(10);
        Random rand = new SecureRandom();
        Collections.shuffle(testList,rand);
        System.out.println(testList);
        NavigableSet<Integer> test = new TreeSet<>(testList);
        System.out.println(test);
        test = new TreeSet<>(new Compparator());
        test.addAll(testList);
        System.out.println(test);

    }

    private List<Integer> getTestList(int n) {
        List<Integer> result = new ArrayList<>();
        for (int i = 1; i <=n ; i++) {
            result.addAll(Collections.nCopies(i,i));
        }
        return result;
    }
}
