package com.portifolio.wealthinker.portfolio.servicesImpl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.portifolio.wealthinker.exceptions.ResourceNotFoundException;
import com.portifolio.wealthinker.portfolio.models.Portfolio;
import com.portifolio.wealthinker.portfolio.models.PortfolioStock;
import com.portifolio.wealthinker.portfolio.repositories.PortfolioRepo;
import com.portifolio.wealthinker.portfolio.repositories.PortfolioStockRepo;
import com.portifolio.wealthinker.portfolio.repositories.StockRepo;
import com.portifolio.wealthinker.portfolio.repositories.TransactionRepo;
import com.portifolio.wealthinker.portfolio.services.PortfolioService;
import com.portifolio.wealthinker.user.models.User;
import com.portifolio.wealthinker.user.repositories.UserRepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepo portfolioRepo;

    private final UserRepo userRepo;

    private final TransactionRepo transactionRepo;

    private final StockRepo stockRepo;

    private final PortfolioStockRepo portfolioStockRepo;

    @Override
    public List<Portfolio> getAllPortfolios(String userId) {
        List<Portfolio> portfolios = portfolioRepo.findByUserId(userId);
        portfolios.forEach(portfolio -> {
            // List<Transaction> transactions = transactionRepo.findByPortfolioId(portfolio.getId());

            // // Map to store aggregated stock data
            // Map<String, StockSummary> stockSummaryMap = new HashMap<>();

            // transactions.forEach(transaction -> {
            //     Stock stock = stockRepo.findById(transaction.getStock().getId()).orElse(null);
            //     if (stock != null) {
            //         String stockId = stock.getId();
            //         StockSummary summary = stockSummaryMap.getOrDefault(stockId, new StockSummary(stock));
            //         summary.addTransaction(transaction);
            //         stockSummaryMap.put(stockId, summary);
            // }
            // });
            // // portfolio.setTransactions(transactions);
            // // Set aggregated data
            // portfolio.setStocksSummary(new ArrayList<>(stockSummaryMap.values()));

            List<PortfolioStock> portfolioStocks = portfolioStockRepo.findPortfolioStocksByPortfolioId(portfolio.getId());
            portfolio.setPortfolioStocks(portfolioStocks);
        });
        return portfolios;
    }

    @Override
    @Transactional
    public Portfolio createPortfolio(Portfolio portfolio, String userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        // String portfolioId = UUID.randomUUID().toString();
        // portfolio.setId(portfolioId);

        // Check if the user has reached the portfolio limit
        long portfolioCount = portfolioRepo.countByUserId(userId);
        if (portfolioCount >= 3) {
            throw new IllegalStateException("You can only create up to 3 portfolios.");
        }

        // Check for duplicate portfolio name
        boolean exists = portfolioRepo.existsByUserIdAndName(userId, portfolio.getName());
        if (exists) {
            throw new IllegalArgumentException("Portfolio with the name '" + portfolio.getName() + "' already exists for this user.");
        }

        portfolio.setUser(user);
        return portfolioRepo.save(portfolio);
    }

    @Override
    public Portfolio getPortfolioById(String portfolioId, String userId) {
        return portfolioRepo.findPortfolioByPortfolioId(portfolioId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Portfolio Not Found"));
    }

    @Override
    public Portfolio updatePortfolio(String portfolioId, Portfolio updatedPortfolio, String userId) {
        Portfolio portfolio = getPortfolioById(portfolioId, userId);
        // Check for duplicate portfolio name
        boolean exists = portfolioRepo.existsByUserIdAndName(userId, portfolio.getName());
        if (exists) {
            throw new IllegalArgumentException("Portfolio with the name '" + portfolio.getName() + "' already exists for this user.");
        }
        portfolio.setName(updatedPortfolio.getName());
        portfolio.setDescription(updatedPortfolio.getDescription());
        return portfolioRepo.save(portfolio);
    }

    @Override
    public void deletePortfolio(String portfolioId, String userId) {
        Portfolio portfolio = portfolioRepo.findById(portfolioId)
        .filter(p -> p.getUser().getId().equals(userId))
        .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found or does not belong to the user."));

        portfolioRepo.delete(portfolio);
    }

}
