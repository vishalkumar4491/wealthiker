package com.portifolio.wealthinker.portfolio.controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portifolio.wealthinker.portfolio.models.PortfolioHistory;
import com.portifolio.wealthinker.portfolio.models.PortfolioStock;
import com.portifolio.wealthinker.portfolio.models.Transaction;
import com.portifolio.wealthinker.portfolio.repositories.PortfolioHistoryRepo;
import com.portifolio.wealthinker.portfolio.services.DashboardService;
import com.portifolio.wealthinker.user.models.User;

import lombok.RequiredArgsConstructor;



@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardservice;

    private final PortfolioHistoryRepo portfolioHistoryRepo;

    @GetMapping
    public String getDashboardPage(Model model, @AuthenticationPrincipal User user) throws Exception {
        List<Transaction> recentTransactions = dashboardservice.getRecentTransactions(user.getId());
        List<PortfolioStock> topPerformingStocks = dashboardservice.getTopPerformingStocks(user.getId());
        model.addAttribute("recentTransactions", recentTransactions);
        model.addAttribute("topPerformingStocks", topPerformingStocks);

        // Fetch portfolio history data
        List<PortfolioHistory> historyList = portfolioHistoryRepo.findByPortfolio_User_IdOrderBySnapshotDateTimeAsc(user.getId());

        // Aggregate data by date
        Map<String, Double> aggregatedInvestments = new LinkedHashMap<>();
        Map<String, Double> aggregatedValues = new LinkedHashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (PortfolioHistory ph : historyList) {
            String date = ph.getSnapshotDateTime().format(formatter);
            aggregatedInvestments.merge(date, ph.getTotalInvestment(), Double::sum);
            aggregatedValues.merge(date, ph.getTotalValue(), Double::sum);
        }

        // Convert maps to lists for Chart.js
        List<String> dates = new ArrayList<>(aggregatedInvestments.keySet());
        List<Double> totalInvestments = new ArrayList<>(aggregatedInvestments.values());
        List<Double> totalValues = new ArrayList<>(aggregatedValues.values());

        // Prepare data for frontend
        Map<String, Object> graphData = new HashMap<>();
        graphData.put("dates", dates);
        graphData.put("totalInvestments", totalInvestments);
        graphData.put("totalValues", totalValues);

        ObjectMapper mapper = new ObjectMapper();
        // String graphDataJson = mapper.writeValueAsString(graphData);
        System.out.println("graphDataJson " + graphData);
        model.addAttribute("graphData", graphData);
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
