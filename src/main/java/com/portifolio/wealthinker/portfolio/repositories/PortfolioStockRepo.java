package com.portifolio.wealthinker.portfolio.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portifolio.wealthinker.portfolio.models.Portfolio;
import com.portifolio.wealthinker.portfolio.models.PortfolioStock;
import com.portifolio.wealthinker.portfolio.models.Stock;

@Repository
public interface PortfolioStockRepo extends JpaRepository<PortfolioStock, String> {
    Optional<PortfolioStock> findByPortfolioAndStock(Portfolio portfolio, Stock stock);
}
