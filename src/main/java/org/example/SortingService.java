package org.example;

import org.example.sort.Sorter;
import org.example.sort.SortingType;
import org.example.sort.exceptions.AbsentSorterException;
import org.example.sort.exceptions.FailedSortingException;
import org.example.sort.exceptions.SizeLimitException;

import java.util.List;

public class SortingService {
    private final List<Sorter> sorters;

    public SortingService(List<Sorter> sorters) {
        this.sorters = sorters;
    }

    public List<Integer> getSortedList(List<Integer> arr, SortingType sortingType) {
        boolean foundSorter = false;
        for (Sorter sorter : sorters) {
            if (sorter.sortingType().equals(sortingType)) {
                foundSorter = true;
                try {
                    return sorter.sort(arr);
                } catch (SizeLimitException e) {
                    System.out.println("Reached length limit for " + sorter.getClass().getName());
                }
            }
        }

        if (!foundSorter) {
            throw new AbsentSorterException("Couldn't find sorter for sortingType=" + sortingType);
        }
        throw new FailedSortingException("Couldn't find any applicable sorters");
    }
}
