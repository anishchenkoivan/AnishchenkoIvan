package org.example.sort;

import org.example.sort.exceptions.SizeLimitException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MergeSorter implements Sorter{
    private final int sizeLimit;

    public MergeSorter(int sizeLimit) {
        this.sizeLimit = sizeLimit;
    }

    @Override
    public List<Integer> sort(List<Integer> arr) {
        if (arr.size() > sizeLimit) {
            throw new SizeLimitException("List exceeds size limit");
        }

        List<Integer> sortedList = new ArrayList<>(arr);
        Collections.sort(sortedList);
        return sortedList;
    }

    public SortingType sortingType() {
        return SortingType.MERGE;
    }
}
