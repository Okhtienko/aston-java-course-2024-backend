package com.java.list.collection;

import com.java.list.utils.CustomList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CustomArrayList<E> implements CustomList<E>, Iterable<E>  {

    private static final int CAPACITY = 10;
    private static final Object[] EMPTY = {};
    private int size;
    private E[] elements;

    public CustomArrayList() {
        size = 0;
        elements = (E[]) new Object[CAPACITY];
    }

    public CustomArrayList(int capacity) {
        size = 0;
        if (capacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + capacity);
        }
        elements = (E[]) (capacity == 0 ? EMPTY : new Object[capacity]);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(E object) {
        return indexOf(object) != -1;
    }

    @Override
    public boolean remove(E object) {
        int index = indexOf(object);
        if (index != -1) {
            System.arraycopy(elements, index + 1, elements, index, size - index - 1);
            elements[--size] = null;
            return true;
        }
        return false;
    }

    @Override
    public void addAll(Collection<? extends E> collection) {
        ensureCapacity(size + collection.size());
        for (E element : collection) {
            elements[size++] = element;
        }
    }

    @Override
    public E[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public void add(int index, E element) {
        check(index);
        ensureCapacity(size + 1);
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }

    @Override
    public void sort(Comparator<? super E> comparator) {
        if (comparator == null) {
            throw new NullPointerException("Comparator can not be null");
        }
        Arrays.sort(elements, 0, size, comparator);
    }

    @Override
    public E get(int index) {
        check(index);
        return elements[index];
    }

    @Override
    public E remove(int index) {
        check(index);
        E element = elements[index];
        int move = size - index - 1;
        if (move > 0) {
            System.arraycopy(elements, index + 1, elements, index, move);
        }
        elements[--size] = null;
        return element;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size && elements[currentIndex] != null;
            }

            @Override
            public E next() {
                return elements[currentIndex++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        for (E element : this) {
            action.accept(element);
        }
    }

    private int indexOf(E object) {
        if (object == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (object.equals(elements[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void check(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            int newCapacity = elements.length + (elements.length >> 1);
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }
}
