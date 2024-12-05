package com.portifolio.wealthinker.portfolio.services;

import java.util.List;

import com.portifolio.wealthinker.portfolio.models.Portfolio;

public interface PortfolioService {
    List<Portfolio> getAllPortfolios(String userId);
    Portfolio createPortfolio(Portfolio portfolio, String userId);
    Portfolio getPortfolioById(String portfolioId, String userId);
    Portfolio updatePortfolio(String portfolioId, Portfolio updatedPortfolio, String userId);
    void deletePortfolio(String portfolioId, String userId);
}
