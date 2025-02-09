package io.github.tbo007.testchamber.collections.list.treelist;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TreeListInternalTest {

    @Test
    void size() {
    }

    @Test
    void isEmpty() {
    }

    @Test
    void contains() {
    }

    @Test
    void iterator() {
    }

    @Test
    void toArray() {
    }

    @Test
    void testToArray() {
    }

    @Test
    void addListNodeChaining() {
        TreeList<TestObject> l = new TreeList<>();
        TestObject a = new TestObject(1, "A");
        l.add(a);
        TestObject b = new TestObject(1, "B");
        l.add(b);
        TestObject c = new TestObject(1, "C");
        l.add(c);
        // get mit dem selben Comp muss bei C rauskommen
        TreeList<TestObject>.ListNode cNode = l.elementData.get(new TestObject(1,"D"));
        assertEquals(c,cNode.object);
        assertEquals(a, cNode.next.object);
        assertEquals(b, cNode.next.next.object);
        assertEquals(c, cNode.next.next.next.object);

        assertEquals(c,cNode.object);
        assertEquals(b, cNode.prev.object);
        assertEquals(a, cNode.prev.prev.object);
        assertEquals(c, cNode.prev.prev.prev.object);


    }

    @Test
    void add() {
    }

    @Test
    void remove() {
    }

    @Test
    void containsAll() {
    }

    @Test
    void addAll() {
    }

    @Test
    void testAddAll() {
    }

    @Test
    void removeAll() {
    }

    @Test
    void retainAll() {
    }

    @Test
    void clear() {
    }

    @Test
    void get() {
    }

    @Test
    void set() {
    }

    @Test
    void testAdd() {
    }

    @Test
    void testRemove() {
    }

    @Test
    void indexOf() {
    }

    @Test
    void lastIndexOf() {
    }

    @Test
    void listIterator() {
    }

    @Test
    void testListIterator() {
    }

    @Test
    void subList() {
    }

    @Test
    void testEquals() {
    }

    @Test
    void testHashCode() {
    }
}