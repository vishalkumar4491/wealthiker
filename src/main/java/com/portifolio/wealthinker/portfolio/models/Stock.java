package com.portifolio.wealthinker.portfolio.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

}
