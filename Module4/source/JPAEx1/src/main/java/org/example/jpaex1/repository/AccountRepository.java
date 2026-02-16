package org.example.jpaex1.repository;

import org.example.jpaex1.domain.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Optional<Account> findByName(String name);
}
