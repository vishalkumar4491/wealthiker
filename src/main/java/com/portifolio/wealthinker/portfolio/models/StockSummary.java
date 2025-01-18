package com.portifolio.wealthinker.portfolio.models;

import com.portifolio.wealthinker.utils.TransactionType;

import lombok.Getter;

@Getter
public class StockSummary {
    private Stock stock;
    private int totalQuantity;
    private double totalValue;

     public StockSummary(Stock stock) {
        this.stock = stock;
        this.totalQuantity = 0;
        this.totalValue = 0.0;
    }

    public void addTransaction(Transaction transaction) {
        if(transaction.getTransactionType() == TransactionType.BUY) {
            this.totalQuantity += transaction.getQuantity();
            totalValue += transaction.getQuantity() * transaction.getPrice();
        }else {
            this.totalQuantity -= transaction.getQuantity();
            totalValue -= transaction.getQuantity() * transaction.getPrice();
        }
    }
}
