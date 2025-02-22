package io.github.tbo007.testchamber.collections.list.treelist;

import org.junit.jupiter.api.Test;

import java.util.List;

public class TreeListTest {

    @Test
    void iterTest () {
        List<String> list = List.of("A","B","C","D", "A");
        List<String> subject = new TreeList<>(list);
        subject.forEach(s -> System.out.print(s + " "));
    }
}
