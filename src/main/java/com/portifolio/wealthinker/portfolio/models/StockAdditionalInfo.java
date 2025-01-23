package com.portifolio.wealthinker.portfolio.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="stock_additional_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockAdditionalInfo {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name="stock_id", nullable=false)
    private Stock stock;    // The stock being added

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name="portfolio_stock_id", nullable=false)
    private PortfolioStock portfolioStock;    // The stock being added

    @ManyToOne
    @JoinColumn(name="portfolio_id", nullable=false)
    private Portfolio portfolio;    // The portfolio to which the stock is being added

    @Lob    // @Lob is used to store large text data
    @Column(columnDefinition="TEXT")
    private String whyAdding;    // Why the stock is being added to the portfolio

    @Lob
    @Column(columnDefinition = "TEXT")
    private String fundamentalCatalysts;  // Answer to "What are the fundamental catalysts?"
    
    @Lob
    @Column(columnDefinition = "TEXT")
    private String risks;  // Answer to "What are the risks?" High risk or low risk
    
    @Lob
    @Column(columnDefinition = "TEXT")
    private String otherNotes;  // Any other relevant information

    @CreationTimestamp
    private LocalDateTime createdAt;
    

}
