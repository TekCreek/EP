package org.example;

public class GenericsDemo2 {
    public static <T extends Number> double add(T number1, T number2) {
        return number1.doubleValue() + number2.doubleValue();
    }

    public static void main(String[] args) {
        System.out.println(add(10, 20)); // valid
        System.out.println(add(10.2, 20.4)); // valid
        // System.out.println(add("Hello", "World")); // invalid
    }

}
