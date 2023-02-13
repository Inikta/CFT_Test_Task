package ru.inikta.task.sort;

import java.io.InvalidClassException;
import java.lang.reflect.Type;
import java.util.Comparator;

public class MergeSorterFabric {
    //symbol by symbol string comparator
    private static final Comparator<String> stringComparator = (s1, s2) -> {
        //compare strings by their characters values
        //returns -1, if string1 is alphabetically higher
        //returns 1, if string1 is alphabetically lower

        int limit = Math.min(s1.length(), s2.length());
        for (int i = 0; i < limit; i++) {
            if (s1.charAt(i) < s2.charAt(i)) {
                return -1;
            } else if (s1.charAt(i) > s2.charAt(i)) {
                return 1;
            }
        }

        //compare strings by their lengths, if they are equal by character values (character sequences are the same)
        //returns -1, if string1 is shorter
        //returns 1, if string1 is longer
        //returns 0, if strings are equal both in length and character values
        return Integer.compare(s1.length(), s2.length());
    };

    //long numbers comparator
    private static final Comparator<String> integerComparator = (stringNum1, stringNum2) -> {
        //parse integers
        long number1 = Long.parseLong(stringNum1);
        long number2 = Long.parseLong(stringNum2);

        return Long.compare(number1, number2);
    };

    /**
     * Fabric method, which creates and returns merge sorter with predefined comparator (according to data type) and taken order.
     * @param dataType type of data to sort
     * @param order ascending or descending order
     * @return merge sorter
     * @throws InvalidClassException when used wrong data type
     */
    public MergeSorter createMergeSorter(Type dataType, SortingOrder order) throws InvalidClassException {
        if (dataType == String.class) {
            return new MergeSorter(stringComparator, order);
        } else if (dataType == Long.class) {
            return new MergeSorter(integerComparator, order);
        } else {
            throw new InvalidClassException("Wrong data type " + "\"" + dataType.getTypeName() + "\".");
        }
    }
}
