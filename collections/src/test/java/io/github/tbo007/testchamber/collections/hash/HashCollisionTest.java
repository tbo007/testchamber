package io.github.tbo007.testchamber.collections.hash;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class HashCollisionTest {

    private static class Wrapper {
        protected final String wrapped;

        protected Wrapper(String wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public int hashCode() {
            return wrapped.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Wrapper wrapper = (Wrapper) o;
            return Objects.equals(wrapped, wrapper.wrapped);
        }
    }


    private static class CompWrapper extends Wrapper
            implements Comparable<CompWrapper> {

        protected CompWrapper(String wrapped) {
            super(wrapped);
        }

        @Override
        public int compareTo(CompWrapper o) {
            return wrapped.compareTo(o.wrapped);
        }
    }

    private static final int N = 3;
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static List<String> COLLIDING_STRINGS;


    @BeforeAll
    public static void setup () {
        COLLIDING_STRINGS = genCollisions(1_000_000).stream().distinct().limit(50_000).toList();
    };


    @Test
    public void sameHash() {
        System.out.println(COLLIDING_STRINGS.size() + "-->" + COLLIDING_STRINGS.stream().map(String::hashCode).distinct().count());

    }

    @Test
    public void noWrapper() {
        HashSet<String> h = new HashSet<>();
        h.addAll(COLLIDING_STRINGS);
        collectionHole(h);
    }

    @Test
    public void wrapperWithoutComparable() {
        HashSet<Wrapper> h = new HashSet<>();
        COLLIDING_STRINGS.stream().map(Wrapper::new).forEach(h::add);
        collectionHole(h);

    }   @Test
    public void wrapperWithComparable() {
        HashSet<CompWrapper> h = new HashSet<>(100_000);
        COLLIDING_STRINGS.stream().map(CompWrapper::new).forEach(h::add);
        collectionHole(h);
    }


    // Util Methods
    public void collectionHole(Collection<?> coll) {
        assertEquals(COLLIDING_STRINGS.size(), coll.size());
    }

    public static List<String> genCollisions(int minCollisions) {
        List<String> strs = genStrings();
        List<String> someCollisions = maxCollisions(strs);
        List<String> moreCollisions = new ArrayList<>(someCollisions);

        while (moreCollisions.size() < minCollisions) {
            List<String> newCollisions = new ArrayList<>();
            for (String s1 : moreCollisions) {
                for (String s2 : someCollisions) {
                    newCollisions.add(s1 + s2);
                }
            }
            moreCollisions.addAll(newCollisions);
        }
        return moreCollisions;
    }


    public static List<String> genStrings() {
        int alphabetLength = ALPHABET.length();
        int wordsLen = (int) Math.pow(alphabetLength, N);
        List<String> words = new ArrayList<>(wordsLen);

        for (int i = 0; i < alphabetLength; i++) {
            for (int j = 0; j < alphabetLength; j++) {
                for (int k = 0; k < alphabetLength; k++) {
                    char[] word = {ALPHABET.charAt(i), ALPHABET.charAt(j), ALPHABET.charAt(k)};
                    words.add(new String(word));
                }
            }
        }
        return words;
    }


    public static List<String> maxCollisions(List<String> words) {
        // HashMap to store the hash values and corresponding list of words
        Map<Integer, List<String>> m = new HashMap<>();

        // Iterate over each word and compute the hash value
        for (String w : words) {
            int h = w.hashCode();
            m.computeIfAbsent(h, k -> new ArrayList<>()).add(w);
        }

        int max = 0;
        int maxK = 0;

        // Find the key with the maximum collisions
        for (Map.Entry<Integer, List<String>> entry : m.entrySet()) {
            int l = entry.getValue().size();
            if (l > max) {
                max = l;
                maxK = entry.getKey();
            }
        }

        // Return the list of words with the maximum collisions
        return m.get(maxK);
    }
}