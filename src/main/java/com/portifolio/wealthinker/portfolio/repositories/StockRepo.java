package com.portifolio.wealthinker.portfolio.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portifolio.wealthinker.portfolio.models.Stock;

@Repository
public interface StockRepo extends JpaRepository<Stock, String> {

    public Optional<Stock> findBySymbol(String symbol);

}
