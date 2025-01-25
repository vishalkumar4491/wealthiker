package com.portifolio.wealthinker.portfolio.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // fetch all transactions for a specific stock in a specific portfolio
    List<Transaction> findByPortfolioIdAndStockSymbol(String portfolioId, String stockSymbol);

    @Query("SELECT t FROM Transaction t WHERE t.stock.id = :stockId AND t.portfolio.user.id = :userId")
    List<Transaction> findByStockIdAndUserId(@Param("stockId") String stockId, @Param("userId") String userId);

    @Query("SELECT t FROM Transaction t WHERE t.portfolio.user.id = :userId ORDER BY t.transactionAt DESC LIMIT 5")
    List<Transaction> findRecentTransactionsForUser(@Param("userId") String userId);

}
