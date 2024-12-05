package com.portifolio.wealthinker.portfolio.servicesImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.portifolio.wealthinker.exceptions.ResourceNotFoundException;
import com.portifolio.wealthinker.portfolio.models.Portfolio;
import com.portifolio.wealthinker.portfolio.repositories.PortfolioRepo;
import com.portifolio.wealthinker.portfolio.services.PortfolioService;
import com.portifolio.wealthinker.user.models.User;
import com.portifolio.wealthinker.user.repositories.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepo portfolioRepo;

    private final UserRepo userRepo;

    @Override
    public List<Portfolio> getAllPortfolios(String userId) {
        return portfolioRepo.findByUserId(userId);
    }

    @Override
    public Portfolio createPortfolio(Portfolio portfolio, String userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        portfolio.setUser(user);
        return portfolioRepo.save(portfolio);
    }

    @Override
    public Portfolio getPortfolioById(String portfolioId, String userId) {
        return portfolioRepo.findById(portfolioId)
            .filter(portfolio -> portfolio.getUser().getId().equals(userId))
            .orElseThrow(() -> new ResourceNotFoundException("Portfolio Not Found"));
    }

    @Override
    public Portfolio updatePortfolio(String portfolioId, Portfolio updatedPortfolio, String userId) {
        Portfolio portfolio = getPortfolioById(portfolioId, userId);
        portfolio.setName(updatedPortfolio.getName());
        portfolio.setDescription(updatedPortfolio.getDescription());
        return portfolioRepo.save(portfolio);
    }

    @Override
    public void deletePortfolio(String portfolioId, String userId) {
        portfolioRepo.deleteById(portfolioId);
    }

}
