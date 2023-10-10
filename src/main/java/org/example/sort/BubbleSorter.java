package org.example.sort;

import org.example.sort.exceptions.SizeLimitException;

import java.util.ArrayList;
import java.util.List;

public class BubbleSorter implements Sorter {
    private final int sizeLimit;

    public BubbleSorter(int sizeLimit) {
        this.sizeLimit = sizeLimit;
    }

    @Override
    public List<Integer> sort(List<Integer> arr) {
        if (arr.size() > sizeLimit) {
            throw new SizeLimitException("List exceeds size limit");
        }

        List<Integer> sortedList = new ArrayList<>(arr);
        for (int i = 0; i < sortedList.size(); i++) {
            for (int j = i + 1; j < sortedList.size(); j++) {
                if (sortedList.get(i) > sortedList.get(j)) {
                    int temp = sortedList.get(i);
                    sortedList.set(i, sortedList.get(j));
                    sortedList.set(j, temp);
                }
            }
        }
        return sortedList;
    }

    @Override
    public SortingType sortingType() {
        return SortingType.BUBBLE;
    }
}
