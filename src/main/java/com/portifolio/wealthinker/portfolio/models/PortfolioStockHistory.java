package com.portifolio.wealthinker.portfolio.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class PortfolioStockHistory {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "portfolio_stock_id", nullable = false)
    private PortfolioStock portfolioStock; // Links to specific stock in a portfolio

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double averagePrice;

    @Column(nullable = false)
    private Double currentPrice;

    @Column(nullable = false)
    private Double totalCurrentValue; // quantity * currentPrice

    @CreationTimestamp
    private LocalDateTime snapshotDateTime;
}
