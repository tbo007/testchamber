package io.github.tbo007.testchamber.collections.list.treelist;

import java.util.Objects;

class TestObject implements Comparable<TestObject> {
    public final Integer comp;
    public final Object obj;

    TestObject(Integer comp, Object obj) {
        this.comp = comp;
        this.obj = obj;
    }

    @Override
    public int compareTo(TestObject o) {
        return comp.compareTo(o.comp);
    }

    @Override
    public String toString() {
        return "O: " + obj + " / C: " + comp;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TestObject that = (TestObject) o;
        return Objects.equals(comp, that.comp) && Objects.equals(obj, that.obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(comp, obj);
    }
}
