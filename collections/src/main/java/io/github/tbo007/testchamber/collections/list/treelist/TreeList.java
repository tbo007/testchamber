package io.github.tbo007.testchamber.collections.list.treelist;



import java.util.*;

/**
 *
 * @param <E>
 *     Suspress erklären
 *     Wenn das Objekt compareTo == 0 immer größer im TreeSet. Wichtig für z.B. contain
 */
@SuppressWarnings("all")
public class TreeList<E> implements List<E> {


    private static final Comparator comparableComp =
            (Object o1, Object o2) -> ((Comparable) o1).compareTo(o2);


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

    private class Iter implements ListIterator<E> {
        private enum Dir {FF,BW}

        private int cursor;
        private Dir direction =null;
        private Iterator<E> elemIter;
        private Iter (int startPos) {
            cursor = startPos;
            position();
        }

        private void position() {
            
        }

        @Override
        public boolean hasNext() {
            return cursor < elementData.size()-1;
        }

        @Override
        public E next() {
            if (Dir.FF.equals(direction)) {
                cursor++;
                return elemIter.next();
            }
            // first nav call. satisying the contract of listIterator(int index)
            if (direction == null) {
                cursor--;
            }
            elemIter = elementData.iterator();
            int pos = 0;
            while (pos <= cursor) {
                elemIter.next();
                pos++;
            }
            direction = Dir.FF;
            cursor++;
            return elemIter.next();

        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0;
        }

        @Override
        public E previous() {
            if (Dir.BW.equals(direction)) {
                cursor--;
                return elemIter.next();
            } else {
                elemIter = elementData.descendingIterator();
                int pos = size()-1;
                while (pos >= cursor) {
                    elemIter.next();
                    pos--;
                }
                direction = Dir.BW;
                cursor--;
                return elemIter.next();
            }

        }

        @Override
        public int nextIndex() {
            return cursor +1;
        }

        @Override
        public int previousIndex() {
            return cursor -1;
        }

        @Override
        public void remove() {
            elemIter.remove();
        }

        @Override
        public void set(E e) {
            throw new UnsupportedOperationException();

        }

        @Override
        public void add(E e) {
            throw new UnsupportedOperationException();

        }
    }

    final Comparator comparator;
    final NavigableSet<E> elementData;

    public TreeList(Comparator<? super E> comparator) {
        this.comparator = comparator;
        this.elementData = new TreeSet(new GlitchHigherComparator(comparator));
    }


    public TreeList() {
        this.comparator = comparableComp;
        this.elementData = new TreeSet(new GlitchHigherComparator(comparator));
    }

    public TreeList(Collection<? extends E> c) {
        this();
        c.forEach(this::add);
    }

    @Override
    public int size() {
        return elementData.size();
    }

    @Override
    public boolean isEmpty() {
        return elementData.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        NavigableSet<E> subset = elementData.tailSet((E) o, true);
        // Wenn Comperator sagt, iter.current >= 1, kann man aufhören
        // denn dann ist man schom zu weit. <0 kann es nicht geben, 0 wenn equals
        // wie bei TreeMap contains und wie gesagt > 0 ist man schon drüber
        // reiocht es, dass erste Element zu prüfen, oder muss man bis zum Ende iterieren
        // TODO: Schreibtisch Test schreiben und mit Marcus reden. die die kleiner gleich sind,
        // würden ja beim compare sonst die plätze tauschen: 1.compare2 vs 2.compare1
       for (Object e : subset) {
           int retval = comparator.compare(e,o);
           if(retval == 0) {
               return true;
           }
       }
        return false;
//        return comparator.compare(subset.iterator().next(),o) == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return elementData.iterator();
    }

    @Override
    public Object[] toArray() {
        return elementData.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return elementData.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return elementData.add(e);
    }

    @Override
    public boolean remove(Object o) {
        NavigableSet<E> subset = elementData.tailSet((E) o, true);
        for(Iterator<E> iiter = subset.iterator();iiter.hasNext();) {
            int retval = comparator.compare(iiter.next(),o);
            if(retval == 0) {
                iiter.remove();
                return true;
            }
        }
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
        elementData.clear();

    }

    @Override
    public E get(int index) {
        return listIterator(index).next();
    }

    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, E element) {
        throw  new UnsupportedOperationException();
    }

    @Override
    public E remove(int index) {
        ListIterator<E> iter = listIterator(index);
        E toRemove = iter.next();
        iter.remove();
        return toRemove;
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
        return new Iter(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new Iter(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return List.of();
    }

    @Override
    public String toString() {
        return elementData.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return elementData.equals(obj);
    }

    @Override
    public int hashCode() {
        return elementData.hashCode();
    }
}