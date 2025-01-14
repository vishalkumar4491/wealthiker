package com.portifolio.wealthinker.portfolio.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.portifolio.wealthinker.utils.SellType;
import com.portifolio.wealthinker.utils.TransactionType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {

    // track every transaction. Each transaction links to a portfolio and specific stock

    @Id
    private String id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="portfolio_id", nullable=false)
    private Portfolio portfolio;    // The portfolio this transaction belongs to

    @ManyToOne(fetch=FetchType.LAZY, optional = false)
    @JoinColumn(name="stock_id", nullable=false)
    private Stock stock;    // The stock this transaction relates to

    @Column(nullable=false)
    private String stockSymbol;

    @Column(nullable=false)
    private String companyName;

    @Column(nullable=false)
    private Double price;    // price per share during transaction

    @Column(nullable=false)
    private Integer quantity;  

    @CreationTimestamp
    private LocalDateTime transactionAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private TransactionType transactionType;  // "BUY" or "SALE"

    @Enumerated(EnumType.STRING)
    @Column(nullable=true)
    private SellType sellType;  // "Selling from specific portfolio or from all portfolio" 

    @Column(nullable=true)
    private Double brokerageFee = 0.0;

    @Column(nullable=true)
    private Double tax = 0.0;

    @Column(name="total_value", nullable=false)
    private Double totalValue = 0.0;

    @Column(name = "notes")
    private String notes;  // optional notes about the transaction

}
