package com.portifolio.wealthinker.portfolio.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "portfolio_stocks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PortfolioStock {
    
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @OneToMany(mappedBy="portfolioStock", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Transaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy="portfolioStock", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<PortfolioStockHistory> portfolioStockHistories = new ArrayList<>();

    @OneToMany(mappedBy="portfolioStock")
    private List<StockAdditionalInfo> additionalInfo = new ArrayList<>();

    @Column(nullable = false)
    private Integer quantity = 0; // Total quantity of the stock in the portfolio

    @Column(nullable = false)
    private Double averagePrice = 0.0; // Average purchase price

    @Column(nullable = false)
    private Double totalValue = 0.0; // Total value of the stock in the portfolio

    @Column(nullable = false)
    private Double latestPrice = 0.0;

    @Column
    private Double unrealizedProfitLoss = 0.0; // Unrealized profit/loss (calculated periodically)

    @Column
    private Double realizedProfitLoss = 0.0; // Realized profit/loss from selling the stock

    @Column
    private Double dividendEarned = 0.0; // Total dividend earned from the stock

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
