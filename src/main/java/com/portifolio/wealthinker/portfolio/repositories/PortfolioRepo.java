package com.portifolio.wealthinker.portfolio.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.portifolio.wealthinker.portfolio.models.Portfolio;

@Repository
public interface PortfolioRepo extends JpaRepository<Portfolio, String>{
    List<Portfolio> findByUserId(String userId);

    boolean existsByUserIdAndName(String userId, String name);

    long countByUserId(String userId); // Counts the number of portfolios for a user

    @Query("SELECT p FROM Portfolio p WHERE p.id=:portfolioId AND p.user.id = :userId")
    Optional<Portfolio> findPortfolioByPortfolioId(@Param("portfolioId") String portfolioId, @Param("userId") String userId);

    @Query("SELECT p FROM Portfolio p WHERE p.user.id=:userId ORDER BY p.totalValue DESC LIMIT 3")
    List<Portfolio> findTopPerformingPortfoliosForUser(String userId);
}
