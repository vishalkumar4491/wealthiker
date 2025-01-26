package com.portifolio.wealthinker.portfolio.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.portifolio.wealthinker.portfolio.models.PortfolioStock;
import com.portifolio.wealthinker.portfolio.models.Transaction;
import com.portifolio.wealthinker.portfolio.services.DashboardService;
import com.portifolio.wealthinker.user.models.User;

import lombok.RequiredArgsConstructor;



@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardservice;

    @GetMapping
    public String getDashboardPage(Model model, @AuthenticationPrincipal User user) {
        List<Transaction> recentTransactions = dashboardservice.getRecentTransactions(user.getId());
        List<PortfolioStock> topPerformingStocks = dashboardservice.getTopPerformingStocks(user.getId());
        model.addAttribute("recentTransactions", recentTransactions);
        model.addAttribute("topPerformingStocks", topPerformingStocks);
        return "dashboard";
    }

    @GetMapping("/recent-transactions")
    public List<Transaction> getRecentTransactions(@AuthenticationPrincipal User user) {
        return dashboardservice.getRecentTransactions(user.getId());
    }

    @GetMapping("/top-performing-stocks")
    public List<PortfolioStock> getTopPerformingStocks(@AuthenticationPrincipal User user) {
        return dashboardservice.getTopPerformingStocks(user.getId());
    }
    
    
    
}
