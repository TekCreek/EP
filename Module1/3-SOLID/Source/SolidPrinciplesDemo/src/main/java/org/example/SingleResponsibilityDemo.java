package org.example;

/**
 * SOLID Principles -
 * SOLID is an acronym for the first five object-oriented design (OOD)
 * principles by Robert C. Martin
 *
 * S - Single Responsibility Principle - A class should have one and only one reason to change.
 * O -
 * L -
 * I -
 * D -
 */

class Account {}

// Interfaces for inversion of control
interface AccountRepository {
    void create(Account account);
}

interface NotificationService {
    void sendWelcome(Account account);
}

class AccountService {
    private final AccountRepository repository;
    private final NotificationService notifications;

    // Dependency injection via constructor
    public AccountService(AccountRepository repo, NotificationService notif) {
        this.repository = repo;
        this.notifications = notif;
    }

    public void openAccount(Account account) {
        // Single responsibility: orchestrate business flow
        fillInternalDetails(account);
        repository.create(account);
        notifications.sendWelcome(account);
    }

    private void fillInternalDetails(Account account) {
        System.out.println("fill account internal details");
    }
}

// Usage with dependency injection
public class SingleResponsibilityDemo {
    public static void main(String[] args) {

        Account account = new Account();
        // fill account details

        AccountRepository repo = new AccountRepositoryImpl();
        NotificationService notif = new NotificationServiceImpl();

        AccountService service = new AccountService(repo, notif);

        service.openAccount(account);
    }
}

class AccountRepositoryImpl implements AccountRepository {
    @Override
    public void create(Account account) {
        // Just for Demo
    }
}

class NotificationServiceImpl implements NotificationService {

    @Override
    public void sendWelcome(Account account) {
        // Send Notification e.g. Email
    }
}
