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
 * D - Dependency Inversion Principle
 *          High level modules should not directly depend on low level modules, instead they should depend
 *          on abstractions.
 */

// Application Layer - High level module
class MyMessenger {
    ProtocolHandler handler;

    public MyMessenger(String protocol) {
        handler = ProtocolHandlerFactory.getProtocolHandler(protocol);
    }

    public void send(String to, String message) {
        handler.sendMessage( "message to - " + to + ", message - " + message );
    }
}

// Transport layer - low level module
// --------------------------------------------------------------------
interface ProtocolHandler {
    void sendMessage(String message);
}

class TCPProtocolHandler implements ProtocolHandler {
    public void sendMessage(String message) {
        System.out.println("TCPProtocolHandler sending message - " + message);
    }
}

class UDPProtocolHandler implements ProtocolHandler {
    public void sendMessage(String message) {
        System.out.println("UDPProtocolHandler sending message - " + message);
    }
}

class ProtocolHandlerFactory {

    public static ProtocolHandler getProtocolHandler(String protocol) {
        if ("TCP".equalsIgnoreCase(protocol)) {
            return new TCPProtocolHandler();
        }

        if ("UDP".equalsIgnoreCase(protocol)) {
            return new UDPProtocolHandler();
        }

        return null;
    }

}
// --------------------------------------------------------------------


public class DependencyInversionEx1 {
    public static void main(String[] args) {
        MyMessenger messenger = new MyMessenger("UDP");
        messenger.send("a", "SOLID is easy");
    }
}
