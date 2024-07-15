package com.java.list.utils;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface CustomList<E> {
    int size();

    boolean isEmpty();

    boolean remove(E object);

    boolean contains(E object);

    void clear();

    void add(int index, E element);

    void forEach(Consumer<? super E> action);

    void sort(Comparator<? super E> comparator);

    void addAll(Collection<? extends E> collection);

    E[] toArray();

    E get(int index);

    E remove(int index);

    Stream<E> stream();
}
