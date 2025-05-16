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
import com.portifolio.wealthinker.portfolio.models.Portfolio;
import com.portifolio.wealthinker.portfolio.models.PortfolioHistory;
import com.portifolio.wealthinker.portfolio.models.PortfolioStock;
import com.portifolio.wealthinker.portfolio.models.Transaction;
import com.portifolio.wealthinker.portfolio.repositories.PortfolioHistoryRepo;
import com.portifolio.wealthinker.portfolio.repositories.PortfolioRepo;
import com.portifolio.wealthinker.portfolio.services.DashboardService;
import com.portifolio.wealthinker.user.models.User;

import lombok.RequiredArgsConstructor;



@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardservice;

    private final PortfolioHistoryRepo portfolioHistoryRepo;

    private final PortfolioRepo portfolioRepo;

    @GetMapping
    public String getDashboardPage(Model model, @AuthenticationPrincipal User user) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Fetch portfolio history data
        List<PortfolioHistory> historyList = portfolioHistoryRepo.findByPortfolio_User_IdOrderBySnapshotDateTimeAsc(user.getId());
        System.out.println("HistoryList Length" + historyList.size());
        // Aggregate data by date
        Map<String, Double> aggregatedInvestments = new LinkedHashMap<>();
        Map<String, Double> aggregatedValues = new LinkedHashMap<>();

        

        for (PortfolioHistory ph : historyList) {
            System.out.println("Portfolio " + ph.getPortfolio().getName() + "Date " + ph.getSnapshotDateTime() + "Invested " + ph.getTotalInvestedValue());

            String date = ph.getSnapshotDateTime().format(formatter);
            aggregatedInvestments.merge(date, ph.getTotalInvestedValue(), Double::sum);
            aggregatedValues.merge(date, ph.getTotalCurrentValue(), Double::sum);
        }

        // Convert maps to lists for Chart.js
        List<String> ndates = new ArrayList<>(aggregatedInvestments.keySet());
        List<Double> ntotalInvestments = new ArrayList<>(aggregatedInvestments.values());
        List<Double> ntotalValues = new ArrayList<>(aggregatedValues.values());

        String currentDate = "";
        Double totalInvested = 0.0;
        Double totalCurrent = 0.0;

        if(!aggregatedInvestments.isEmpty()){
            currentDate = ndates.get(ndates.size() - 1);
            totalInvested = aggregatedInvestments.get(currentDate);
            totalCurrent = aggregatedValues.get(currentDate);
        }
        model.addAttribute("currentDate", currentDate);
        model.addAttribute("totalInvested", totalInvested);
        model.addAttribute("totalCurrent", totalCurrent);

        System.out.println("Datessssss " + currentDate + " totalInvested " + totalInvested + " totalCurrent " + totalCurrent);

        // Prepare data for frontend
        Map<String, Object> graphData = new HashMap<>();
        graphData.put("dates", ndates);
        graphData.put("totalInvestments", ntotalInvestments);
        graphData.put("totalValues", ntotalValues);

        // ObjectMapper mapper = new ObjectMapper();
        // String graphDataJson = mapper.writeValueAsString(graphData);
        System.out.println("graphDataJson " + graphData);
        model.addAttribute("graphData", graphData);

        List<Transaction> recentTransactions = dashboardservice.getRecentTransactions(user.getId());
        List<PortfolioStock> topPerformingStocks = dashboardservice.getTopPerformingStocks(user.getId());
        model.addAttribute("recentTransactions", recentTransactions);
        model.addAttribute("topPerformingStocks", topPerformingStocks);

        // Top performing Portfolios
        List<Portfolio> topPortfolios = dashboardservice.getTopPerformingPortfolios(user.getId());
        // model.addAttribute("topPortfolios", topPortfolios);

        for(Portfolio portfolio : topPortfolios){
            List<PortfolioHistory> singlePortfolioHistories = portfolioHistoryRepo.findByPortfolioIdOrderBySnapshotDateTimeAsc(portfolio.getId());

            // Use LinkedHashMap to preserve order
            Map<String, Double> aggInvestments = new LinkedHashMap<>();
            Map<String, Double> aggValues = new LinkedHashMap<>();
            Map<String, Double> aggNetGains = new LinkedHashMap<>();

            for (PortfolioHistory ph : singlePortfolioHistories) {
                String date = ph.getSnapshotDateTime().format(formatter);
                System.out.println("Date format " + date);
                // aggInvestments.merge(date, ph.getTotalInvestedValue(), Double::sum);
                aggInvestments.put(date, ph.getTotalInvestedValue());
                // aggValues.merge(date, ph.getTotalCurrentValue(), Double::sum);
                aggValues.put(date, ph.getTotalCurrentValue());
                aggNetGains.merge(date, ph.getNetGains(), Double::sum);
            }

            List<String> dates = new ArrayList<>(aggInvestments.keySet());
            List<Double> totalInvestments = new ArrayList<>(aggInvestments.values());
            List<Double> totalValues = new ArrayList<>(aggValues.values());
            List<Double> netGains = new ArrayList<>(aggNetGains.values());

            // Create a map for mini chart data.
            Map<String, Object> miniChartData = new HashMap<>();
            miniChartData.put("dates", dates);
            miniChartData.put("totalInvestments", totalInvestments);
            miniChartData.put("totalValues", totalValues);
            miniChartData.put("netGains", netGains);

            // Attach the aggregated mini chart data to the portfolio.
            portfolio.setMiniChartData(miniChartData);
        }

        model.addAttribute("topPortfolios", topPortfolios);

        System.out.println("PortfolioLength " + topPortfolios.size());

        
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
