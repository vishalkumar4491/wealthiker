package com.portifolio.wealthinker.expense.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portifolio.wealthinker.expense.models.RecurringTransaction;
import com.portifolio.wealthinker.expense.utils.TransactionType;


@Repository
public interface RecurringTransactionRepo extends JpaRepository<RecurringTransaction, Long> {

    // get all recurring entries
    List<RecurringTransaction> findByUserId(String userId);

    // filter by transaction
    List<RecurringTransaction> findByUserIdAndTransactionType(String userId, TransactionType type);

}
