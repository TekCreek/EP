package org.example;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamsDemo1 {
    public static void main(String[] args) {

        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");

        // Filtering Demo
        List<String> filteredNames = names.stream()
                .filter(name -> name.startsWith("A"))
                .collect(Collectors.toList());

        System.out.println(filteredNames); // Output: [Alice]

        // Mapping Demo

        List<String> upperCaseNames = names.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        System.out.println(upperCaseNames); // Output: [ALICE, BOB, CHARLIE, DAVID]

        // Reduce Demo

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        int sum = numbers.stream()
                .reduce(0, Integer::sum);

        System.out.println(sum); // Output: 15

    }
}
