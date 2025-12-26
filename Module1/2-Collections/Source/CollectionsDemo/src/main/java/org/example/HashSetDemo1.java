package org.example;

import java.util.HashSet;
import java.util.Objects;

/**
 * Custom classes with Collections -
 *      - If working with hash structures we need to implement hashCode and
 *        equals methods.
 *      - If working with tree structures we need to implement Comparable interface
 *        or we need to provide Comparator.
 */
public class HashSetDemo1 {
    public static void main(String[] args) {
        HashSet<Fruit> set = new HashSet<>();

        set.add(new Fruit("apple"));
        set.add(new Fruit("mango"));
        set.add(new Fruit("apple")); // should not be added

        System.out.println( set );
    }
}

class Fruit {
    private String name;

    public Fruit(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (! (o instanceof  Fruit) || o == null)
//            return false;
//
//        return this.name.equals( ((Fruit)o).name);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(name);
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fruit fruit = (Fruit) o;
        return Objects.equals(name, fruit.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
