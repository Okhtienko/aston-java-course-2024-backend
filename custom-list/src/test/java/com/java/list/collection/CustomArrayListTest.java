package com.java.list.collection;

import com.java.list.utils.CustomList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomArrayListTest {

    @Test
    void testConstructorException() {
        assertThrows(IllegalArgumentException.class, () -> new CustomArrayList<>(-1));
    }

    @Test
    void testConstructorEmpty() {
        CustomList<Integer> list = new CustomArrayList<>(0);
        assertTrue(list.isEmpty());
    }

    @Test
    void testSize() {
        CustomList<Integer> list = new CustomArrayList<>();
        list.addAll(List.of(1, 2, 3));
        assertEquals(3, list.size());
    }

    @Test
    void testEmptyListTrue() {
        CustomList<Integer> list = new CustomArrayList<>();
        assertTrue(list.isEmpty());
    }

    @Test
    void testEmptyListFalse() {
        CustomList<Integer> list = new CustomArrayList<>();
        list.addAll(List.of(1, 2, 3));
        assertFalse(list.isEmpty());
    }

    @Test
    void testContainsTrue() {
        CustomList<Integer> list = new CustomArrayList<>();
        list.addAll(List.of(1, 2, 3));
        assertTrue(list.contains(1));
    }

    @Test
    void testContainsFalse() {
        CustomList<Integer> list = new CustomArrayList<>();
        assertFalse(list.contains(1));
    }

    @Test
    void testContainsNull() {
        CustomList<Integer> list = new CustomArrayList<>();
        list.add(0, null);
        assertTrue(list.contains(null));
    }

    @Test
    void testRemoveTrue() {
        CustomList<Integer> list = new CustomArrayList<>();
        list.addAll(List.of(1, 2, 3));
        assertTrue(list.remove(Integer.valueOf(1)));
    }

    @Test
    void testRemoveFalse() {
        CustomList<Integer> list = new CustomArrayList<>();
        assertFalse(list.remove(Integer.valueOf(1)));
    }

    @Test
    void testAddAllSuccess() {
        CustomList<Integer> list = new CustomArrayList<>();
        list.addAll(List.of(1, 2, 3, 4, 5));
        assertEquals(5, list.size());
    }

    @Test
    void testAddAllEmptyList() {
        CustomList<Integer> list = new CustomArrayList<>();
        list.addAll(new ArrayList<>());
        assertTrue(list.isEmpty());
    }

    @Test
    void testAddAllWithNullElements() {
        CustomList<Integer> list = new CustomArrayList<>();
        List<Integer> collection = new ArrayList<>(Arrays.asList(1, null, 3));

        list.addAll(collection);

        assertEquals(3, list.size());
        assertNull(list.get(1));
    }

    @Test
    void testClearEmptyList() {
        CustomList<Integer> list = new CustomArrayList<>();
        list.addAll(List.of(1, 2, 3));
        list.clear();
        assertEquals(0, list.size());
    }

    @Test
    void testAddAtCorrectIndex() {
        CustomList<Integer> list = new CustomArrayList<>();

        list.add(0, 1);
        list.add(1, 2);
        list.add(2, 3);

        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test
    void testAddSuccess() {
        CustomList<Integer> list = new CustomArrayList<>();
        list.add(0, 1);
        list.add(1, 2);
        list.add(2, 3);
        list.add(1, 4);

        assertEquals(4, list.size());
        assertEquals(1, list.get(0));
        assertEquals(4, list.get(1));
        assertEquals(2, list.get(2));
        assertEquals(3, list.get(3));
    }

    @Test
    void testAddAtIndexNegative() {
        CustomList<Integer> list = new CustomArrayList<>();
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, 1));
    }

    @Test
    void testSortSuccess() {
        CustomList<Integer> list = new CustomArrayList<>();
        list.addAll(List.of(1, 3, 2));

        list.sort(Comparator.naturalOrder());

        assertArrayEquals(List.of(1, 2, 3).toArray(), list.toArray());
    }

    @Test
    void testSortWithNullComparator() {
        CustomList<Integer> list = new CustomArrayList<>();
        assertThrows(NullPointerException.class, () -> list.sort(null));
    }

    @Test
    void testGet() {
        CustomList<Integer> list = new CustomArrayList<>();

        list.add(0, 1);
        list.add(1, 2);
        list.add(2, 3);

        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test
    void testGetNegativeIndex() {
        CustomList<Integer> list = new CustomArrayList<>();
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
    }

    @Test
    void testRemoveSuccess() {
        CustomList<Integer> list = new CustomArrayList<>();

        list.add(0,1);
        list.add(1, 2);

        int element = list.remove(0);

        assertEquals(1, element);
    }

    @Test
    void testRemoveFromEmptyList() {
        CustomList<Integer> list = new CustomArrayList<>();
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(3));
    }

    @Test
    void testToArray() {
        CustomList<Integer> list = new CustomArrayList<>();
        list.addAll(List.of(1, 2, 3));

        Object[] array = list.toArray();

        assertEquals(3, array.length);
        assertEquals(1, array[0]);
        assertEquals(2, array[1]);
        assertEquals(3, array[2]);
    }
}
