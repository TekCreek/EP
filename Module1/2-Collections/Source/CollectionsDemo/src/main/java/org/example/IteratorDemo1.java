package org.example;

import java.util.*;

// Iterator
public class IteratorDemo1 {

    private static void display(Collection<Integer> col) {
// // For each value in the collection.
//        for (Integer value : col) {
//            System.out.println( value );
//        }

        Iterator<Integer> iterator = col.iterator();
        while (iterator.hasNext()) {
            Integer value = iterator.next();
            System.out.println( value );
        }
    }

    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(10);
        list.add(20);
        list.add(30);
        list.add(10);

        System.out.println("values in the list are...");
        display(list);

        HashSet<Integer> set = new HashSet<>();
        set.add(10);
        set.add(20);
        set.add(30);
        set.add(10); // not added.

        System.out.println("values in the set are...");
        display(set);
    }
}
