package com.portifolio.wealthinker.portfolio.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class PortfolioStockHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "portfolio_stock_id", nullable = false)
    private PortfolioStock portfolioStock; // Links to specific stock in a portfolio

    @Column(nullable = false)
    private Double quantity;

    @Column(nullable = false)
    private Double averagePrice;

    @Column(nullable = false)
    private Double latestPrice;

    @Column(nullable = false)
    private Double value; // quantity * latestPrice

    @Column(nullable = false)
    private LocalDateTime snapshotDateTime;
}
