package com.portifolio.wealthinker.expense.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portifolio.wealthinker.expense.models.Account;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {
    // Get all accounts for a user
    List<Account> findByUserId(String userId);

    // find by name and user for validation
    Optional<Account> findByNameAndUserId(String name, String userId);
}
