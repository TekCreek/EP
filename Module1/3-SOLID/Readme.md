# Solid Principles

SOLID principles are five fundamental guidelines for object-oriented design that promote maintainable, scalable, and flexible code. Introduced by Robert C. Martin, they help avoid common pitfalls like rigid or fragile systems.

The five SOLID principles are:

## S - Single Responsibility Principle 

A class should have one and only one reason to change.

```java

class AccountService {
    private AccountRepository accountRepository;
    private NotificationService notificationService;

    public AccountService(AccountRepository accountRepository, NotificationService notificationService) {
        this.accountRepository = accountRepository;
        this.notificationService = notificationService;
    }

    public void createAccount(User user) {
        accountRepository.save(user);
        notificationService.sendWelcomeEmail(user);
    }
}
```
In this example, AccountService has a Single reason to change:

* Modify AccountRepository if database logic changes.
* Modify NotificationService if messaging logic changes.
* Modify AccountService only if the account creation process itself changes.

## O - Open Closed Principle 

Objects or entities should be open for extension but closed for modification.

## L - Liskov Substitution Principl

Objects of super class shall be replaced with objects of subclasses.

## I - Interface Segregation Principle

A client should never be forced to implement an interface that it does not use.

## D - Dependency Inversion Principle

High level modules should not directly depend on low level modules, instead they should depend on abstractions.
