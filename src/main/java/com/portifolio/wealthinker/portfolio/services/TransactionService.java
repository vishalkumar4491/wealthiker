package com.portifolio.wealthinker.portfolio.services;

import java.util.List;
import java.util.Map;

import com.portifolio.wealthinker.portfolio.models.Portfolio;
import com.portifolio.wealthinker.portfolio.models.Transaction;

public interface TransactionService {
    List<Transaction> getTransactionsByPortfolio(String portfolioId);

    List<Transaction> getTransactionsByStockAcrossPortfolios(String stockId);

    List<Transaction> getTransactionsByStockInPortfolio(String portfolioId, String stockId);

    List<Transaction> getAllTransactions();

    Map<Portfolio, List<Transaction>> getTransactionsGroupedByPortfolioForStock(String stockId);
}
