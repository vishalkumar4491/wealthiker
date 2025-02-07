package com.portifolio.wealthinker.portfolio.servicesImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.portifolio.wealthinker.portfolio.models.Portfolio;
import com.portifolio.wealthinker.portfolio.models.PortfolioStock;
import com.portifolio.wealthinker.portfolio.models.Transaction;
import com.portifolio.wealthinker.portfolio.repositories.PortfolioRepo;
import com.portifolio.wealthinker.portfolio.repositories.PortfolioStockRepo;
import com.portifolio.wealthinker.portfolio.repositories.TransactionRepo;
import com.portifolio.wealthinker.portfolio.services.DashboardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final TransactionRepo transactionRepo;
    private final PortfolioStockRepo portfolioStockRepo;
    private final PortfolioRepo portfolioRepo;

    @Override
    public List<Transaction> getRecentTransactions(String userId) {
        return transactionRepo.findRecentTransactionsForUser(userId);
    }

    @Override
    public List<PortfolioStock> getTopPerformingStocks(String userId) {
        return portfolioStockRepo.findTopPerformingStocksForUser(userId);
    }

    @Override
    public List<Portfolio> getTopPerformingPortfolios(String userId) {
        return portfolioRepo.findTopPerformingPortfoliosForUser(userId);
    }

}
