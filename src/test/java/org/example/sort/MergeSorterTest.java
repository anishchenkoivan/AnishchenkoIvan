package org.example.sort;

import org.example.sort.MergeSorter;
import org.example.sort.exceptions.SizeLimitException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MergeSorterTest {
    boolean sorted(List<Integer> arr) {
        for (int i = 1; i < arr.size(); i++) {
            if (arr.get(i) < arr.get(i - 1)) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void sizeLimitTest() {
        MergeSorter mergeSorter = new MergeSorter(2);
        assertDoesNotThrow(() -> mergeSorter.sort(new ArrayList<>(400)));
        assertThrows(SizeLimitException.class, () -> mergeSorter.sort(List.of(1, 2, 3)));
    }

    @Test
    public void sortTest() {
        List<Integer> originalList = new ArrayList<>();
        MergeSorter mergeSorter = new MergeSorter(500);
        List<Integer> sortedList;

        for (int i = 0; i < 100; i++) {
            originalList.add((int) (Math.random() * 100));
        }
        sortedList = mergeSorter.sort(originalList);

        assertNotSame(originalList, sortedList);
        assertTrue(sorted(sortedList));
    }
}
