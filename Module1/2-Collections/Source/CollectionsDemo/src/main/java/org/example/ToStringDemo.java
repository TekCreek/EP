package org.example;
// toString() of the Object class.

public class ToStringDemo {
    public static void main(String[] args) {
        Sample s = new Sample();
        System.out.println( s );
        // System.out.println( s.toString() );
    }
}

// Object
class Sample {
    @Override
    public String toString() {
        return "Hello everone!!!";
    }
}