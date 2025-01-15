package com.portifolio.wealthinker.portfolio.servicesImpl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.portifolio.wealthinker.portfolio.models.Portfolio;
import com.portifolio.wealthinker.portfolio.models.Transaction;
import com.portifolio.wealthinker.portfolio.repositories.PortfolioRepo;
import com.portifolio.wealthinker.portfolio.repositories.StockRepo;
import com.portifolio.wealthinker.portfolio.repositories.TransactionRepo;
import com.portifolio.wealthinker.portfolio.services.TransactionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepo transactionRepo;

    private final PortfolioRepo portfolioRepo;

    private final StockRepo stockRepo;
    @Override
    public List<Transaction> getTransactionsByPortfolio(String portfolioId) {
        return transactionRepo.findByPortfolioId(portfolioId);
    }

    @Override
    public List<Transaction> getTransactionsByStockAcrossPortfolios(String stockId) {
        return transactionRepo.findByStockId(stockId);
    }

    @Override
    public List<Transaction> getTransactionsByStockInPortfolio(String portfolioId, String stockId) {
        return transactionRepo.findByPortfolioIdAndStockId(portfolioId, stockId);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepo.findAll();
    }

    @Override
    public Map<Portfolio, List<Transaction>> getTransactionsGroupedByPortfolioForStock(String stockId) {
        List<Transaction> transactions = transactionRepo.findByStockId(stockId);
        return transactions.stream().collect(Collectors.groupingBy(Transaction::getPortfolio));
    }

    @Override
    public void sellStockFromPortfolio(String stockSymbol, String portfolioId, int quantity) {
        List<Transaction> transactions = transactionRepo.findByPortfolioIdAndStockSymbol(portfolioId, stockSymbol);
        System.out.println("Transaction: " + transactions.size());
        int netQuantity = 0;
        for (Transaction transaction : transactions) {
            System.out.println("Transaction: " + transaction.getQuantity());
            netQuantity += transaction.getQuantity();
        }
        if (netQuantity >= quantity) {
            // sell the stock
            netQuantity -= quantity;
        }else{
            throw new IllegalArgumentException("Insufficient quantity in portfolio");
        }
    }

}
