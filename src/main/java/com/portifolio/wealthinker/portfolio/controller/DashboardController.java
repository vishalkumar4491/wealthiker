package com.portifolio.wealthinker.portfolio.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.portifolio.wealthinker.portfolio.services.DashboardService;
import com.portifolio.wealthinker.user.models.User;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestParam;

import com.portifolio.wealthinker.portfolio.models.Stock;
import com.portifolio.wealthinker.portfolio.models.Transaction;



@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardservice;

    @GetMapping
    public String getDashboardPage() {
        return "dashboard";
    }

    @GetMapping("/recent-transactions")
    public List<Transaction> getRecentTransactions(@AuthenticationPrincipal User user) {
        return dashboardservice.getRecentTransactions(user.getId());
    }

    @GetMapping("/top-performing-stocks")
    public List<Stock> getTopPerformingStocks() {
        return dashboardservice.getTopPerformingStocks();
    }
    
    
    
}
