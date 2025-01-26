package com.portifolio.wealthinker.portfolio.services;

import java.util.List;

import com.portifolio.wealthinker.portfolio.models.PortfolioStock;
import com.portifolio.wealthinker.portfolio.models.Transaction;

public interface DashboardService {
    List<Transaction> getRecentTransactions(String userId);
    List<PortfolioStock> getTopPerformingStocks(String userId);
}
