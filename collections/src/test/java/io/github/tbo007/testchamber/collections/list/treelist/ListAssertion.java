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

        // Iter
        int refSize = reference.size();
        assertEquals(reference.listIterator(refSize).previous(),
                subject.listIterator(refSize).previous(), "ListIter(size).prev");
        assertEquals(reference.listIterator(0).next(),
                subject.listIterator(0).next(), "ListIter(0).next");

        // comod...

    }
}
