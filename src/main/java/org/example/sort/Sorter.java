package org.example.sort;

import java.util.List;

public interface Sorter {
    List<Integer> sort(List<Integer> arr);
    SortingType sortingType();
}
