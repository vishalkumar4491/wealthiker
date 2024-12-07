package com.portifolio.wealthinker.portfolio.services;

import java.util.List;

import com.portifolio.wealthinker.portfolio.models.Transaction;

public interface TransactionService {
    List<Transaction> getTransactionsByPortfolio(String portfolioId);

    List<Transaction> getTransactionsByStockAcrossPortfolios(String stockId);

    List<Transaction> getTransactionsByStockInPortfolio(String portfolioId, String stockId);
}
