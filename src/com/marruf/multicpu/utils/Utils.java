package com.marruf.multicpu.utils;

import com.google.common.collect.PeekingIterator;

import java.util.Comparator;
import java.util.Iterator;

import static com.google.common.base.Preconditions.checkArgument;


public class Utils {

    public static <T> Iterable<T> uniqueIterable(final Iterator<T> iterator) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return iterator;
            }
        };
    }

    public static <T> T getSmallest(Comparator<? super T> comparator, T... items) {
        checkArgument(items.length != 0, "items is empty");

        T smallest = null;
        for (T item : items) {
            if (item == null) continue;
            if (smallest == null || comparator.compare(item, smallest) < 0) {
                smallest = item;
            }
        }
        return smallest;
    }

    public static <T> void nextWhichMaches(T next, PeekingIterator<? extends T>... iterators) {
        for (PeekingIterator<? extends T> i : iterators) {
            if (next.equals(i.peek())) {
                i.next();
            }
        }
    }

    // Prevents instantiation
    private Utils() {
        throw new AssertionError("Cannot instantiate object from " + this.getClass());
    }
}
