package com.portifolio.wealthinker.expense.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portifolio.wealthinker.expense.models.Category;
import com.portifolio.wealthinker.expense.utils.CategoryType;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long>{

    // fetch all categories(income or expense)
    List<Category> findByUserIdAndType(String userId, CategoryType type);

    // find category by name
    Optional<Category> findByNameAndUserId(String name, String userId);
}
