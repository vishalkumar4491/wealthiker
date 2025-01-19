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
public class PortfolioHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @Column(nullable = false)
    private Double totalValue; // Sum of (quantity * latestPrice) for all stocks in the portfolio

    @Column(nullable = false)
    private Double totalInvestment; // Sum of (quantity * averagePrice) for all stocks in the portfolio

    @Column(nullable = false)
    private Double netGains; // totalValue - totalInvestment

    @Column(nullable = false)
    private LocalDateTime snapshotDateTime;
}
