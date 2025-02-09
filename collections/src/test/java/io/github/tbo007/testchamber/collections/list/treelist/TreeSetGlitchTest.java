package io.github.tbo007.testchamber.collections.list.treelist;

import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.*;

public class TreeSetGlitchTest {

    @SuppressWarnings("all")
    private static  final Comparator comparableComp =
            (Object o1, Object o2) -> ((Comparable) o1).compareTo(o2);

    @SuppressWarnings("all")
    private static class GlitchHigherComparator implements Comparator {

        private  final Comparator decoratedComp;

        private GlitchHigherComparator(Comparator decoratedComp) {
            this.decoratedComp = decoratedComp;
        }
        @Override
        public int compare(Object o1, Object o2) {
            return decoratedComp.compare(o1, o2) >= 0 ? 1 : -1;
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
        test = new TreeSet(new GlitchHigherComparator(comparableComp));
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
