package com.portifolio.wealthinker.portfolio.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portifolio.wealthinker.portfolio.models.Transaction;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, String>{

    // fetch all transactions for a portfolio
    List<Transaction> findByPortfolioId(String portfolioId);

    // fetch all transactions for a specific stock across all portfolios
    List<Transaction> findByStockId(String stockId);

    // fetch all transactions for a specific stock in a specific portfolio
    List<Transaction> findByPortfolioIdAndStockId(String portfolioId, String stockId);
}
