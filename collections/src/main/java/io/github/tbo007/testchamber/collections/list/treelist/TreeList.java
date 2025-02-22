package io.github.tbo007.testchamber.collections.list.treelist;

import java.util.*;


/**
 * ListImplementation that ensures a sorted state upon add / Remove.
 * Null Not allowed
 *
 * @param <E>
 */
public class TreeList <E>  implements List<E> {

    NavigableMap<E,ListNode> elementData;
    private final Comparator<? super E> comparator;

    private  int size = 0;

    /* Double Linked Data Structure:

     */
    class ListNode  {
        final E object;
        int pos = 1;
        ListNode prev;
        ListNode next;

        private ListNode(E object) {
            this.object = object;
            prev = this;
            next = this;
        }

        public boolean hasNext() {
            return next != this;
        }

        public boolean hasPrev() {
            return  prev != this;
        }

        public ListNode next() {
            return next;

        }
        public ListNode prev() {
            return prev;

        }
    }

    // TODO: Concurent Mod / No Such Element Exception
    class ListIter implements ListIterator<E> {

        private enum Direction {
            FORWARD,
            BACKWARD
        }

        private ListNode current;
        private int cursor;
        private Direction direction;
        private E currentKey;
        private Iterator<Map.Entry<E,ListNode>> elementDataIter;
        private ListNode currentNode;


        public ListIter(int startIndex) {
            this.cursor = startIndex;
        }

        @Override
        public boolean hasNext() {
            return cursor < size();
        }

        @Override
        public E next() {
            if (direction != Direction.FORWARD) {
                direction = Direction.FORWARD;
                position();
            }
            if (currentNode != null && currentNode.hasNext()) {
                ListNode toReturn = currentNode.next;
                currentNode = toReturn.hasNext() ? toReturn: null;
                cursor++;
                return toReturn.object;
            }

            if (currentNode == null) {
                Map.Entry<E, ListNode> next = elementDataIter.next();
                currentKey = next.getKey();
                currentNode = next.getValue();
            }
            // the first Node is always the last added. @see #add()
            ListNode toReturn = currentNode.next;
            currentNode = toReturn.hasNext() ? toReturn: null;
            cursor++;
            return toReturn.object;
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0;
        }

        @Override
        public E previous() {
            if (direction != Direction.BACKWARD) {
                direction = Direction.BACKWARD;
                position();
            }
            if (currentNode != null && currentNode.hasPrev()) {
                ListNode toReturn = currentNode.prev;
                currentNode = toReturn.hasPrev() ? toReturn: null;
                cursor--;
                return toReturn.object;
            }

            if (currentNode == null) {
                Map.Entry<E, ListNode> next = elementDataIter.next();
                currentKey = next.getKey();
                currentNode = next.getValue();
                // the first Node is always the last added. @see #add()
                currentNode = currentNode.next;
            }

            ListNode toReturn = currentNode.prev;
            currentNode = toReturn.hasPrev() ? toReturn: null;
            cursor--;
            return toReturn.object;
        }

        @Override
        public int nextIndex() {
            return cursor+1;
        }

        @Override
        public int previousIndex() {
            return cursor-1;
        }

        @Override
        public void remove() {

        }

        @Override
        public void set(E e) {
            throw new UnsupportedOperationException();

        }

        @Override
        public void add(E e) {
            throw new UnsupportedOperationException();

        }

        /** Auf den aktuellen CursorStand bringen oder Navigation umdrehen **/
        private void position() {
            currentNode = null;

            NavigableMap<E,ListNode> currentMapPortion;
            if (currentKey != null) {
                currentMapPortion = direction == Direction.FORWARD ?
                        elementData.tailMap(currentKey,true) :
                        elementData.headMap(currentKey,true);
            } else{
                currentMapPortion = direction == Direction.FORWARD ? elementData :
                        elementData.descendingMap();
            }
            elementDataIter = currentMapPortion.entrySet().iterator();

            // Wenn nicht am Anfang oder Ende der Liste, zur richtige Stelle "spulen"
            if(cursor != 0 && cursor != size()-1 && currentKey == null) {
                // Wenn FORWARD, dann von 0 zum Index laufen, ansonsten von size()-1
                // rückwärts. Was mit remove() machen ?
                if(direction == Direction.FORWARD) {
                    int i = 0;
                    while (i <= cursor ) {
                        next();
                        i++;
                    }
                }else {
                    int i = size()-1;
                    while (cursor > i) {
                        previous();
                        i--;
                    }
                }
            }
        }
    }



    public TreeList(Comparator<? super E> comparator) {
        this.comparator= comparator;
        this.elementData = new TreeMap<>(comparator);
    }


    public TreeList() {
        this((Comparator<? super E>) null);
    }

    public TreeList(Collection<? extends E> c) {
        this((Comparator<? super E>) null);
        c.forEach(this::add);
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
        return listIterator();
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
        size++;
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
    public boolean  addAll(Collection<? extends E> c) {
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
        return listIterator(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListIter(index);
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

    // package Methods


}
