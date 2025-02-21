package com.portifolio.wealthinker.portfolio.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.portifolio.wealthinker.portfolio.models.Transaction;
import com.portifolio.wealthinker.portfolio.services.TransactionService;
import com.portifolio.wealthinker.user.models.User;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    // fetch all transactions in a portfolio
    @RequestMapping("/portfolio/{portfolioId}")
    public String getTransactionsByPortfolio(@PathVariable String portfolioId, Model model) {
        model.addAttribute("transactions", transactionService.getTransactionsByPortfolio(portfolioId));
        return "portfolio/transactions/portfolio_transactions";
    }

    // fetch all transactions for a stock across all portfolios
    @RequestMapping("/stock/{stockId}")
    public String getTransactionsByStockAcrossPortfolios(@PathVariable String stockId, Model model) {
        model.addAttribute("transactions", transactionService.getTransactionsByStockAcrossPortfolios(stockId));
        return "portfolio/transactions/stock_transactions";
    }

    // fetch all transactions for a stock in a portfolio
    @RequestMapping("/portfolio/{portfolioId}/stock/{stockId}")
    public String getTransactionsByStockInPortfolio(@PathVariable String portfolioId, @PathVariable String stockId, Model model) {
        model.addAttribute("transactions", transactionService.getTransactionsByStockInPortfolio(portfolioId, stockId));
        return "portfolio/transactions/stock_in_portfolio_transactions";
    }

    // Fetch all transactions across the system
    @GetMapping("/all")
    public String getAllTransactions(Model model, @AuthenticationPrincipal User user) {
        List<Transaction> transactions = transactionService.getAllTransactions(user.getId());
        model.addAttribute("transactions", transactions);
        return "portfolio/transactions/all_transactions";
    }
}
