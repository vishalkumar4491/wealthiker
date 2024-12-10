package com.portifolio.wealthinker.portfolio.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name="stocks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Stock {

    @Id
    private String id;

     @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString(); // Set UUID if not already set
        }
    }

    @Column(unique=true, nullable=false)
    private String symbol;

    @Column(nullable=false)
    private String name;

    @Column(nullable=true)
    private String sector;

    @Column(name="market-price", nullable=true)
    private Double marketPrice; // latest market price of stock

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy="stock", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Transaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy="stock")
    private List<StockAdditionalInfo> additionalInfo = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false) // Ensure this matches your DB schema
    private Portfolio portfolio;

}
