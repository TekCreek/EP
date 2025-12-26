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
 * I -
 * D -
 */

class Vehicle {
    public void start() {
        System.out.println("starting a vehicle");
    }
    public void stop() {
        System.out.println("stopping a vehicle");
    }
}

class Bike extends Vehicle {
    public void start() {
        System.out.println("starting a bike");
    }
    public void stop() {
        System.out.println("stopping a bike");
    }
}

class Car extends Vehicle {
    public void start() {
        System.out.println("starting a car");
        // throw new RuntimeException("boom you are gone..");
    }
    public void stop() {
        System.out.println("stopping a car");
    }
}

public class LiskovSubstitutionEx1 {

    static void testDrive(Vehicle vehicle) {
        vehicle.start();
        vehicle.stop();
    }

    public static void main(String[] args) {
        testDrive(new Vehicle());
        testDrive(new Bike());
        testDrive(new Car());
    }
}
