package com.portifolio.wealthinker.portfolio.servicesImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.portifolio.wealthinker.exceptions.ResourceNotFoundException;
import com.portifolio.wealthinker.portfolio.models.Portfolio;
import com.portifolio.wealthinker.portfolio.models.Stock;
import com.portifolio.wealthinker.portfolio.models.StockSummary;
import com.portifolio.wealthinker.portfolio.models.Transaction;
import com.portifolio.wealthinker.portfolio.repositories.PortfolioRepo;
import com.portifolio.wealthinker.portfolio.repositories.StockRepo;
import com.portifolio.wealthinker.portfolio.repositories.TransactionRepo;
import com.portifolio.wealthinker.portfolio.services.PortfolioService;
import com.portifolio.wealthinker.user.models.User;
import com.portifolio.wealthinker.user.repositories.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepo portfolioRepo;

    private final UserRepo userRepo;

    private final TransactionRepo transactionRepo;

    private final StockRepo stockRepo;

    @Override
    public List<Portfolio> getAllPortfolios(String userId) {
        List<Portfolio> portfolios = portfolioRepo.findByUserId(userId);
        portfolios.forEach(portfolio -> {
            List<Transaction> transactions = transactionRepo.findByPortfolioId(portfolio.getId());

            // Map to store aggregated stock data
            Map<String, StockSummary> stockSummaryMap = new HashMap<>();

            transactions.forEach(transaction -> {
                Stock stock = stockRepo.findById(transaction.getStock().getId()).orElse(null);
                if (stock != null) {
                    String stockId = stock.getId();
                    StockSummary summary = stockSummaryMap.getOrDefault(stockId, new StockSummary(stock));
                    summary.addTransaction(transaction);
                    stockSummaryMap.put(stockId, summary);
            }
            });
            // portfolio.setTransactions(transactions);
            // Set aggregated data
            portfolio.setStocksSummary(new ArrayList<>(stockSummaryMap.values()));
        });
        return portfolioRepo.findByUserId(userId);
    }

    @Override
    public Portfolio createPortfolio(Portfolio portfolio, String userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        String portfolioId = UUID.randomUUID().toString();
        portfolio.setId(portfolioId);
        portfolio.setUser(user);
        return portfolioRepo.save(portfolio);
    }

    @Override
    public Portfolio getPortfolioById(String portfolioId, String userId) {
        return portfolioRepo.findById(portfolioId)
            .filter(portfolio -> portfolio.getUser().getId().equals(userId))
            .orElseThrow(() -> new ResourceNotFoundException("Portfolio Not Found"));
    }

    @Override
    public Portfolio updatePortfolio(String portfolioId, Portfolio updatedPortfolio, String userId) {
        Portfolio portfolio = getPortfolioById(portfolioId, userId);
        portfolio.setName(updatedPortfolio.getName());
        portfolio.setDescription(updatedPortfolio.getDescription());
        return portfolioRepo.save(portfolio);
    }

    @Override
    public void deletePortfolio(String portfolioId, String userId) {
        portfolioRepo.deleteById(portfolioId);
    }

}
