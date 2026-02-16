package org.example.jpaex1;

import org.example.jpaex1.domain.Account;
import org.example.jpaex1.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class JpaEx1ApplicationTests {

    @Autowired
    AccountRepository accountRepository;

    @Test
    void findByIdTest() {
        System.out.println("************** Find By Id Output: *************** ");
        Optional<Account> acc = accountRepository.findById(1L);
        if (acc.isEmpty()) {
            System.out.println("Account not found ");
        } else {
            Account a = acc.get();
            System.out.println("Account details - ID : " + a.getId());
            System.out.println(" NAME : " + a.getName());
            System.out.println(" BALANCE : " + a.getBalance());
        }
    }


    @Test
    void findAllTest() {
        System.out.println("************** Find All Output: *************** ");
        for (Account  a: accountRepository.findAll() ) {
            System.out.println("Account details - ID : " + a.getId());
            System.out.println(" NAME : " + a.getName());
            System.out.println(" BALANCE : " + a.getBalance());
        }
    }

    @Test
    void findByNameTest() {
        System.out.println("************** Find By Id Output: *************** ");
        Optional<Account> acc = accountRepository.findByName("a");
        if (acc.isEmpty()) {
            System.out.println("Account not found ");
        } else {
            Account a = acc.get();
            System.out.println("Account details - ID : " + a.getId());
            System.out.println(" NAME : " + a.getName());
            System.out.println(" BALANCE : " + a.getBalance());
        }
    }

    @Test
    void createAccountTest() {
        System.out.println("************** Create Account Output: *************** ");
        Account a = new Account();
        a.setName("newAcc1");
        a.setBalance(4000);
        accountRepository.save(a);
        System.out.println("ID : " + a.getId());
    }   

    @Test
    void deleteAccountTest() {
        System.out.println("************** Delete Account Output: *************** ");
        accountRepository.deleteById(1L);   
    }

    @Test
    void updateAccountTest() {
        System.out.println("************** Update Account Output: *************** ");
        Optional<Account> acc = accountRepository.findById(2L);
        if (acc.isEmpty()) {
            System.out.println("Account not found ");
        } else {
            Account a = acc.get();
            a.setBalance(5000);
            accountRepository.save(a);
        }
    }
}
