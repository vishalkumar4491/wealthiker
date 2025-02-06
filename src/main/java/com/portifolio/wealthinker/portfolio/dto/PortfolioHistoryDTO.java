package com.portifolio.wealthinker.portfolio.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PortfolioHistoryDTO {
    private String date;  // formatted date string (yyyy-MM-dd)
    private Double totalInvestment;
    private Double totalValue;

    // Constructor that accepts a java.sql.Date
    public PortfolioHistoryDTO(Date date, Double totalInvestment, Double totalValue) {
        // Format the date as "yyyy-MM-dd"
        this.date = new SimpleDateFormat("yyyy-MM-dd").format(date);
        this.totalInvestment = totalInvestment;
        this.totalValue = totalValue;
    }

    // Getters and setters (or use Lombok annotations like @Data)
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getTotalInvestment() {
        return totalInvestment;
    }

    public void setTotalInvestment(Double totalInvestment) {
        this.totalInvestment = totalInvestment;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }
}
