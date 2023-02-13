package ru.inikta.task.sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergeSorter {
    private final Comparator<String> comparator;
    private final int modifier;

    /**
     * Merge sorting algorithm, which recursively sorts list of strings.
     *
     * @param comparator strings comparator
     * @param mode       sorting order (ascending or descending)
     */
    public MergeSorter(Comparator<String> comparator, SortingOrder mode) {
        this.comparator = comparator;
        if (mode == SortingOrder.DESCENDING) {
            modifier = -1;
        } else {
            modifier = 1;
        }
    }

    /**
     * Sorting method, which sorts strings list by recursive variation of merge sort algorithm.
     *
     * @param currentList list to sort
     * @return sorted copy of the initial list
     */
    public List<String> sort(List<String> currentList) {
        //return list as it is, if it has only one element
        if (currentList.size() == 1) {
            return currentList;
        }


        //sort right and left half independently to merge them in one list later
        List<String> leftHalf = sort(currentList.subList(0, currentList.size() / 2));
        List<String> rightHalf = sort(currentList.subList(currentList.size() / 2, currentList.size()));

        //return merged halves
        return merge(rightHalf, leftHalf);
    }

    /**
     * Merging part of merge sort algorithm. Halves are almost equal in size and may differ by one element.
     * @param leftHalf left half of the initial list
     * @param rightHalf right half of the initial list
     * @return sorted copy of list
     */
    private List<String> merge(List<String> leftHalf, List<String> rightHalf) {
        List<String> resultingList = new ArrayList<>();
        //initialize right and left halves "pointers" (counters) and limit for the cycle
        int resultSize = leftHalf.size() + rightHalf.size();
        int leftCounter = 0;
        int rightCounter = 0;
        while (leftCounter + rightCounter != resultSize) {
            //if left half is passed, add all elements of right half
            if (leftCounter >= leftHalf.size()) {
                resultingList.add(rightHalf.get(rightCounter++));
            }
            //if right half is passed, add all elements of left half
            else if (rightCounter >= rightHalf.size()) {
                resultingList.add(leftHalf.get(leftCounter++));
            }
            //else compare first elements of halves and add the one, which is lesser or equal to resulting list
            else {
                switch (modifier * comparator.compare(leftHalf.get(leftCounter), rightHalf.get(rightCounter))) {
                    case 0, 1 -> resultingList.add(rightHalf.get(rightCounter++));
                    case -1 -> resultingList.add(leftHalf.get(leftCounter++));
                }
            }
        }
        return List.copyOf(resultingList);
    }
}
