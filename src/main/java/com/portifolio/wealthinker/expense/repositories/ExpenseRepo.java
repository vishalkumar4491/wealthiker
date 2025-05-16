package com.portifolio.wealthinker.expense.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portifolio.wealthinker.expense.models.Expense;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense, Long> {

    // get all expense
    List<Expense> findByUserId(String userId);

    // Get expense for a date range for statistics
    List<Expense> findByUserIdAndDateBetween(String userId, LocalDate startDate, LocalDate endDate);

    // Filter by cayegory
    List<Expense> findByUserIdAndCategoryId(String userId, Long categoryId);

    // top 5 recent expenses
    List<Expense> findTop5ByUserIdOrderByDateDesc(String userId);

    // search expenses by tag
    List<Expense> findByUserIdAndTagsContaining(String userId, String tag);

}
