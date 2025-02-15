package io.github.tbo007.testchamber.collections.list.treelist;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ListAssertion {

    final List<?> reference;
    final List<?> subject;

    public ListAssertion(List<?> reference, List<?> subject) {
        this.reference = reference;
        this.subject = subject;
    }

    public void  assertOk () {
        assertEquals(reference.size(),subject.size(), "Size");
        assertEquals(reference.isEmpty(),subject.isEmpty(),"isEmpty");
    }
}
