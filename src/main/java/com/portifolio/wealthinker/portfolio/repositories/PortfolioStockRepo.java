package com.portifolio.wealthinker.portfolio.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.portifolio.wealthinker.portfolio.models.Portfolio;
import com.portifolio.wealthinker.portfolio.models.PortfolioStock;
import com.portifolio.wealthinker.portfolio.models.Stock;

@Repository
public interface PortfolioStockRepo extends JpaRepository<PortfolioStock, String> {
    Optional<PortfolioStock> findByPortfolioAndStock(Portfolio portfolio, Stock stock);

    @Query("SELECT ps FROM PortfolioStock ps WHERE ps.portfolio.id = :portfolioId ORDER BY ps.totalCurrentValue DESC")
    List<PortfolioStock> findPortfolioStocksByPortfolioId(@Param("portfolioId") String portfolioId);

    @Query("SELECT ps FROM PortfolioStock ps WHERE ps.portfolio.user.id = :userId ORDER BY (ps.currentPrice - ps.averagePrice)/ps.averagePrice DESC")
    List<PortfolioStock> findTopPerformingStocksForUser(String userId);
}
