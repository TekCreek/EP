package org.example;

import java.util.TreeSet;

// Remove duplicates
public class TreeSetDemo2 {
    public static void main(String[] args) {
        int a[] = {30, 10, 20, 40, 10, 20, 10};

        // HashSet<Integer> set1 = new HashSet<Integer>();
        // LinkedHashSet<Integer> set1 = new LinkedHashSet<Integer>();
        TreeSet<Integer> set1 = new TreeSet<Integer>();
        for (int ele : a) {
            if (! set1.add(ele) ) {
                System.out.println("duplicate found - " + ele);
            }
        }

        // System.out.println( set1 ); // set1.toString()

        // access individual elements of the collections.
        System.out.println("Values in the collection are ");
        for (Integer value: set1) {
            System.out.println( value );
        }

    }
}

