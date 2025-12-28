package org.example;

/**
 * SOLID Principles -
 * SOLID is an acronym for the first five object-oriented design (OOD)
 * principles by Robert C. Martin
 *
 * S - Single Responsibility Principle -
 *          A class should have one and only one reason to change.
 * O - Open Closed Principle -
 *          Objects or entities should be open for extension but
 *          closed for modification.
 * L - Liskov Substitution Principle
 *          Objects of super class shall be replaced with objects of
 *          subclasses.
 * I - Interface Segregation Principle
 *          A client should never be forced to implement an interface that it does not use.
 * D -
 */

interface VegRestaurant {
    void vegMeals();
}

interface NonVegRestaurant {
    void nonVegMeals();
}

class ABCVegRestaurant implements VegRestaurant {

    @Override
    public void vegMeals() {
        System.out.println("provide veg meals");
    }
}

class XYZRestaurant implements VegRestaurant, NonVegRestaurant {

    @Override
    public void vegMeals() {
        System.out.println("provide veg meals");
    }

    @Override
    public void nonVegMeals() {
        System.out.println("provide non veg meals");
    }
}

public class InterfaceSegDemo {
    public static void main(String[] args) {

    }
}
