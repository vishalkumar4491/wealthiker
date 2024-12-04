package com.portifolio.wealthinker.portfolio.models;

import java.time.LocalDateTime;
import java.util.List;

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
@Table(name="portfolios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Portfolio {

    // user's investment portfolio

    @Id
    private String id;

    @Column(nullable=false)
    private String name;  // Portfolio name (ex: Retirement portfolio)

    @Column(nullable=false)
    private Boolean isActive = true;  // soft delete mechanism

    @Column(name="total_value", nullable=false)
    private Double totalValue = 0.0;

    @Column
    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;  // Linked to User entity

    @OneToMany(mappedBy="portfolio", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<Transaction> transactions;  // List of associated stock transactions

}
