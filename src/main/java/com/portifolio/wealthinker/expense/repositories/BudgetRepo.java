package com.portifolio.wealthinker.expense.repositories;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portifolio.wealthinker.expense.models.Budget;

@Repository
public interface BudgetRepo extends JpaRepository<Budget, Long> {

    List<Budget> findByUserId(String userId);

    // budget by category and month
    Optional<Budget> findByUserIdAndCategoryIdAndMonth(String userId, Long categoryId, YearMonth month);

}
