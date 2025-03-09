package io.github.tbo007.testchamber.collections.list.treelist;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TreeListTest {

    @Test
    void iterTest () {
        List<String> list = new ArrayList<>(List.of("A","B","C","D", "A"));
        Collections.sort(list);
        List<String> subject = new TreeList<>(list);
        subject.listIterator().forEachRemaining(
        s -> System.out.print(s + " "));
    }

    @Test
    void iterSwitchDirTest () {
        List<String> list = new ArrayList<>(List.of("A","B","C","D"));
        Collections.sort(list);
        List<String> subject = new TreeList<>(list);
        ListIterator<String> iter = subject.listIterator(2);
        System.out.println(subject);
        assertEquals("C", iter.next(), "Third Elem in List");
        assertEquals("B", iter.previous(), "Second Elem in List");
        assertEquals("C", iter.next(), "Third Elem in List");
        assertEquals("D", iter.next(), "forth Elem in List");
        assertEquals("C", iter.previous(), "Third Elem in List");
        assertEquals("B", iter.previous(), "Second Elem in List");
        assertEquals("C", iter.next(), "Third Elem in List");
    }

    @Test
    void iterMaxPosition () {
        List<String> list = new ArrayList<>(List.of("A","B","C","D"));
        Collections.sort(list);
        List<String> subject = new TreeList<>(list);
        ListIterator<String> iterSubject = subject.listIterator(0);
        ListIterator<String> iterKnowCorrect = list.listIterator(0);
        System.out.println(subject);
        assertEquals(iterKnowCorrect.next(), iterSubject.next(), "First Elem in List");
        iterSubject = subject.listIterator(list.size()-1);
        iterKnowCorrect = list.listIterator(list.size()-1);
        assertEquals(iterKnowCorrect.previous(), iterSubject.previous(), "Last Elem in List");
    }



    @Test
    void internalIterTest () {
        List<String> list = new ArrayList<>(List.of("A","B","B","B","C","D", "A"));

        Collections.sort(list);
        TreeList<String> subject = new TreeList<>(list);
        Iterator<String> iterator = subject.elementData.headSet("B", true).descendingIterator();
        iterator.forEachRemaining(System.out::println);
    }


}
