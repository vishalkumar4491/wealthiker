package com.portifolio.wealthinker.portfolio.servicesImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.portifolio.wealthinker.portfolio.models.Transaction;
import com.portifolio.wealthinker.portfolio.repositories.TransactionRepo;
import com.portifolio.wealthinker.portfolio.services.TransactionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepo transactionRepo;
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

}
