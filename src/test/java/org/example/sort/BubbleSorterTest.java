package org.example.sort;

import org.example.sort.BubbleSorter;
import org.example.sort.exceptions.SizeLimitException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class BubbleSorterTest {
    boolean sorted(List<Integer> arr) {
        for (int i = 1; i < arr.size(); i++) {
            if (arr.get(i) < arr.get(i - 1)) {
                return false;
            }
        }
        return true;
    }
    @Test
    public void testSizeLimit() {
        BubbleSorter bubbleSorter = new BubbleSorter(2);
        assertDoesNotThrow(() -> bubbleSorter.sort(new ArrayList<>()));
        assertThrows(SizeLimitException.class, () -> bubbleSorter.sort(List.of(1, 2, 3)));
    }
    @Test
    public void testSort() {
        List<Integer> originalList = new ArrayList<>();
        BubbleSorter bubbleSorter = new BubbleSorter(500);
        List<Integer> sortedList;

        for (int i = 0; i < 100; i++) {
            originalList.add((int)(Math.random() * 100));
        }
        sortedList = bubbleSorter.sort(originalList);

        assertNotSame(originalList, sortedList);
        assertTrue(sorted(sortedList));
    }
}
