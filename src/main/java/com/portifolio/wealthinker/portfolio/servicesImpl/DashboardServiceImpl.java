package com.portifolio.wealthinker.portfolio.servicesImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.portifolio.wealthinker.portfolio.models.Stock;
import com.portifolio.wealthinker.portfolio.models.Transaction;
import com.portifolio.wealthinker.portfolio.repositories.TransactionRepo;
import com.portifolio.wealthinker.portfolio.services.DashboardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final TransactionRepo transactionRepo;

    @Override
    public List<Transaction> getRecentTransactions(String userId) {
        return transactionRepo.findRecentTransactionsForUser(userId);
    }

    @Override
    public List<Stock> getTopPerformingStocks() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTopPerformingStocks'");
    }

}
