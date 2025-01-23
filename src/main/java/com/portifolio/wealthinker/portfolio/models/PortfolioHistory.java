package com.portifolio.wealthinker.portfolio.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PortfolioHistory {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @Column(nullable = false)
    private Double totalValue; // Sum of (quantity * latestPrice) for all stocks in the portfolio

    @Column(nullable = false)
    private Double totalInvestment; // Sum of (quantity * averagePrice) for all stocks in the portfolio

    @Column(nullable = false)
    private Double netGains; // totalValue - totalInvestment

    @CreationTimestamp
    private LocalDateTime snapshotDateTime;
}
