package com.portifolio.wealthinker.portfolio.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.portifolio.wealthinker.portfolio.models.Portfolio;
import com.portifolio.wealthinker.portfolio.services.PortfolioService;
import com.portifolio.wealthinker.user.models.User;

import lombok.RequiredArgsConstructor;




@Controller
@RequestMapping("/portfolios")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    // Get all portfolios
    @GetMapping
    public String getAllPortfolios(Model model, @AuthenticationPrincipal User user) {
        List<Portfolio> portfolios = portfolioService.getAllPortfolios(user.getId());
        model.addAttribute("portfolios", portfolios);
        model.addAttribute("userId", user.getId());
        return "portfolio/all_portfolios";
    }

    // get single portfolio
    @GetMapping("/{portfolioId}")
    public String getPortfolioById(@PathVariable String portfolioId, Model model, @AuthenticationPrincipal User user) {
        Portfolio portfolio = portfolioService.getPortfolioById(portfolioId, user.getId());
        model.addAttribute("portfolio", portfolio);
        model.addAttribute("userId", user.getId());
        return "portfolio/portfolio_details";
    }

    // create portfolio form
    @GetMapping("/new")
    public String createPortfolioForm(Model model) {
        model.addAttribute("portfolio", new Portfolio());
        return "portfolio/add_portfolio";
    }

    // Processing form
    @PostMapping
    public String createPortfolio(@ModelAttribute Portfolio portfolio, @AuthenticationPrincipal User user) {
        portfolioService.createPortfolio(portfolio, user.getId());
        return "redirect:/portfolios";
    }
    
    @GetMapping("/{id}/edit")
    public String editPortfolioForm(@PathVariable String id, Model model, @AuthenticationPrincipal User user) {
        Portfolio portfolio = portfolioService.getPortfolioById(id, user.getId());
        model.addAttribute("portfolio", portfolio);
        return "portfolio/edit_portfolio";
    }

    @PostMapping("/{id}/update")
    public String updatePortfolio(@PathVariable String id, @ModelAttribute Portfolio portfolio, @AuthenticationPrincipal User user) {
        portfolioService.updatePortfolio(id, portfolio, user.getId());
        return "redirect:/portfolios";
    }

    @PostMapping("/{id}/delete")
    public String deletePortfolio(@PathVariable String id, @AuthenticationPrincipal User user) {
        portfolioService.deletePortfolio(id, user.getId());
        return "redirect:/portfolios";
    }
    

}
