package org.example;

@FunctionalInterface
interface MyAdder {
    int add(int a, int b);
}

public class FunctionalInterfaceDemo1 {
    public static void main(String[] args) {

        // with Lambda
        MyAdder adder = (a, b) -> a + b;

        int result = adder.add(10, 20);

        System.out.println(result);
    }
}
