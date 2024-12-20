package com.portifolio.wealthinker.portfolio.services;

import java.util.List;

import com.portifolio.wealthinker.portfolio.models.Stock;
import com.portifolio.wealthinker.portfolio.models.StockAdditionalInfo;

public interface StockService {
    Stock getStockById(String id);
    List<Stock> searchStocks(String keyword);
    Stock getStockDetails(String Symbol);
    void addStockAdditionalInfo(StockAdditionalInfo info);
    Stock saveOrGetStock(Stock stock);
}
