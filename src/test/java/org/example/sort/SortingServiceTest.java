package org.example.sort;

import org.example.SortingService;
import org.example.sort.exceptions.AbsentSorterException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SortingServiceTest {
    @Test
    public void sortingServiceTest() {
        final boolean[] sorted = {false};
        Sorter sorter = new Sorter() {
            @Override
            public List<Integer> sort(List<Integer> arr) {
                sorted[0] = true;
                return null;
            }

            @Override
            public SortingType sortingType() {
                return SortingType.BUBBLE;
            }
        };
        SortingService sortingService = new SortingService(List.of(sorter));

        assertDoesNotThrow(() -> sortingService.getSortedList(new ArrayList<>(), SortingType.BUBBLE));
        assertThrows(AbsentSorterException.class, () -> sortingService.getSortedList(new ArrayList<>(), SortingType.MERGE));
        assertTrue(sorted[0]);
    }
}
