package com.portifolio.wealthinker.portfolio.services;

import java.util.List;
import java.util.Map;

import com.portifolio.wealthinker.portfolio.models.Portfolio;
import com.portifolio.wealthinker.portfolio.models.Transaction;
import com.portifolio.wealthinker.user.models.User;

public interface TransactionService {
    List<Transaction> getTransactionsByPortfolio(String portfolioId);

    List<Transaction> getTransactionsByStockAcrossPortfolios(String stockId);

    List<Transaction> getTransactionsByStockInPortfolio(String portfolioId, String stockId);

    List<Transaction> getAllTransactions(String userId);

    Map<Portfolio, List<Transaction>> getTransactionsGroupedByPortfolioForStock(String stockId, User loggedInUser);
    
    void sellStockFromPortfolio(String stockSymbol, String portfolioId, int quantity);

    void sellStockFromTotalHoldings(String stockSymbol, int quantity);
}
