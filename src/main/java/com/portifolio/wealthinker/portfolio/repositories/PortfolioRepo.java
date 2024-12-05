package com.portifolio.wealthinker.portfolio.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portifolio.wealthinker.portfolio.models.Portfolio;

@Repository
public interface PortfolioRepo extends JpaRepository<Portfolio, String>{
    List<Portfolio> findByUserId(String userId);
}
