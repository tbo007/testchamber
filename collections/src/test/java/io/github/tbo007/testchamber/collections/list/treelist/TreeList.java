package io.github.tbo007.testchamber.collections.list.treelist;

import java.util.*;


/**
 * ListImplementation that ensures a sorted state upon add / Remove.
 * Null Allowed
 *
 * @param <E>
 */
public class TreeList <E>  implements List<E> {

    private class ListNode  {
        final E object;
        int pos = 1;
        ListNode prev;
        ListNode next;

        private ListNode(E object) {
            this.object = object;
            prev = this;
            next = this;

        }
    }

    private NavigableMap<E,ListNode> elementData;
    private final Comparator<? super E> comparator;

    private final int size = 0;

    public TreeList(Comparator<? super E> comparator) {
        this.comparator= comparator;
        this.elementData = new TreeMap<>(comparator);
    }


    public TreeList() {
        this((Comparator<? super E>) null);
    }

    public TreeList(Collection<? extends E> c) {
        this((Comparator<? super E>) null);
        // TODO impl;
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    /**
     * A->A
     * A->B->A (B->A) ..A
     *  (A->B->C)..>C
     *
     * **/
    @Override
    public boolean add(E e) {
        ListNode newNode = new ListNode(e);
        ListNode oldNode = elementData.put(e, newNode);
        if(oldNode == null) {
            return true;
        }
        newNode.next = oldNode.next;
        newNode.prev = oldNode;
        oldNode.next = newNode;
        newNode.next.prev = newNode;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        elementData = new TreeMap<>(comparator);

    }

    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();

    }

    @Override
    public E remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return List.of();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
