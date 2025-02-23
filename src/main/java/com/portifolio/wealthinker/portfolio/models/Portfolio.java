package com.portifolio.wealthinker.portfolio.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.portifolio.wealthinker.user.models.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="portfolios", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "name"}))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Portfolio {

    // user's investment portfolio

    @Id
    private String id = UUID.randomUUID().toString();;

    @Column(nullable=false)
    private String name;  // Portfolio name (ex: Retirement portfolio)

    @Column(nullable=false)
    private Boolean isActive = true;  // soft delete mechanism

    @Column(name="total_invested_value", nullable=false)
    private Double totalInvestedValue = 0.0;

    @Column(name="total_current_value", nullable=false)
    private Double totalCurrentValue = 0.0;

    @Column
    private Double unrealizedProfitLoss = 0.0; // Unrealized profit/loss (calculated periodically)

    @Column
    private Double realizedProfitLoss = 0.0; // Realized profit/loss from selling the stock

    @Column
    private String description;

    // @Transient
    // private List<StockSummary> stocksSummary;  // for total stock in a portfolio

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Transient field for mini chart data; not persisted in DB.
    @Transient
    private Map<String, Object> miniChartData;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;  // Linked to User entity

    @OneToMany(mappedBy="portfolio", fetch=FetchType.LAZY)
    private List<Transaction> transactions;  // List of associated stock transactions

    @OneToMany(mappedBy="portfolio", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<StockAdditionalInfo> additionalInfo = new ArrayList<>();  // Associated stock additional info

    @OneToMany(mappedBy="portfolio", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<PortfolioStock> portfolioStocks = new ArrayList<>();

    @OneToMany(mappedBy="portfolio", fetch=FetchType.LAZY)
    private List<PortfolioHistory> portfolioHistories = new ArrayList<>();
}
