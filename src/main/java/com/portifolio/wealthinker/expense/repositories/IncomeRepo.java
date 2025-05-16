package com.portifolio.wealthinker.expense.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portifolio.wealthinker.expense.models.Income;

@Repository
public interface IncomeRepo extends JpaRepository<Income, Long> {
    // get all Income
    List<Income> findByUserId(String userId);

    // Get Income for a date range for statistics
    List<Income> findByUserIdAndDateBetween(String userId, LocalDate startDate, LocalDate endDate);

    // Filter by cayegory
    List<Income> findByUserIdAndCategoryId(String userId, Long categoryId);

    // top 5 recent Income
    List<Income> findTop5ByUserIdOrderByDateDesc(String userId);

    // search Income by tag
    List<Income> findByUserIdAndTagsContaining(String userId, String tag);
}
