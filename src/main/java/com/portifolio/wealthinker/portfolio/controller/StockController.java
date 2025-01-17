package com.portifolio.wealthinker.portfolio.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.portifolio.wealthinker.portfolio.models.Portfolio;
import com.portifolio.wealthinker.portfolio.models.Stock;
import com.portifolio.wealthinker.portfolio.models.StockAdditionalInfo;
import com.portifolio.wealthinker.portfolio.models.Transaction;
import com.portifolio.wealthinker.portfolio.repositories.TransactionRepo;
import com.portifolio.wealthinker.portfolio.services.PortfolioService;
import com.portifolio.wealthinker.portfolio.services.StockService;
import com.portifolio.wealthinker.portfolio.services.TransactionService;
import com.portifolio.wealthinker.utils.SellType;
import com.portifolio.wealthinker.utils.TransactionType;

import lombok.RequiredArgsConstructor;



@Controller
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    private final PortfolioService portfolioService;

    private final TransactionService transactionService;

    private final TransactionRepo transactionRepo;

    // stock search page
    @GetMapping("/search")
    public String searchStockPage(@RequestParam String portfolioId, @RequestParam String userId, Model model) {
        model.addAttribute("portfolioId", portfolioId);
        model.addAttribute("userId", userId);
        return "stock/search";
    }

    // Handle stock search request
    @GetMapping("/search/results")
    public String searchStockResults(@RequestParam String keyword, @RequestParam String portfolioId, @RequestParam String userId, Model model) {
        List<Stock> stocks = stockService.searchStocks(keyword);
        System.out.println("Stocks List" + stocks);
        model.addAttribute("stocks", stocks);
        model.addAttribute("userId", userId);
        model.addAttribute("keyword", keyword);
        model.addAttribute("portfolioId", portfolioId);
        return "stock/results";
    }

    // stock add page
    @GetMapping("/add/{symbol}")
    public String addStockPage(@PathVariable String symbol, @RequestParam String portfolioId, @RequestParam String userId, @RequestParam String name, Model model) {
        Stock stock = stockService.getStockDetails(symbol);
        stock.setName(name);
        Portfolio portfolio = portfolioService.getPortfolioById(portfolioId, userId);
        model.addAttribute("portfolio", portfolio);
        model.addAttribute("userId", userId);
        model.addAttribute("stock", stock);
        return "stock/add";
    }

    // sell stock page
    @GetMapping("/sell/{symbol}")
    public String sellStockPage(@PathVariable String symbol, @RequestParam String portfolioId, @RequestParam String userId, @RequestParam String name, Model model) {
        Stock stock = stockService.getStockDetails(symbol);
        stock.setName(name);
        Portfolio portfolio = portfolioService.getPortfolioById(portfolioId, userId);
        model.addAttribute("portfolio", portfolio);
        model.addAttribute("userId", userId);
        model.addAttribute("stock", stock);
        return "stock/sell";
    }

    // Handle stock add request
    @PostMapping("/add")
    public String postMethodName(@RequestParam String portfolioId, 
            @RequestParam String userId, @RequestParam String symbol, @RequestParam String name, 
            @RequestParam(required = false) String whyAdding,
            @RequestParam(required = false) String fundamentalCatalysts,
            @RequestParam(required = false) String risks,
            @RequestParam(required = false) String otherNotes,
            @RequestParam Double price,
            @RequestParam Double marketPrice,
            @RequestParam Integer quantity,
            @RequestParam TransactionType transactionType,
            @RequestParam(required = false) SellType sellType) {
        
        // Validate and fetch portfolio for the given user
        Portfolio portfolio = portfolioService.getPortfolioById(portfolioId, userId);
        // Stock stock = stockService.getStockDetails(symbol);
        Stock stock = stockService.getStockBySymbol(symbol);
        if (stock == null) {
            stock = new Stock();
            stock.setId(UUID.randomUUID().toString()); // Generate UUID if not set
           
        }
        
        stock.setName(name);
        stock.setSymbol(symbol);
        stock.setPortfolio(portfolio);
        stock.setMarketPrice(marketPrice);

        System.out.println("Stock Details" + stock.getId() + " " + stock.getSymbol() + " " + stock.getName() + " " + stock.getPortfolio());

        
        if(transactionType == TransactionType.SELL) {
            if(sellType == SellType.PORTFOLIO) {
                transactionService.sellStockFromPortfolio(stock.getSymbol(), portfolioId, quantity);
            }
        }

        stock = stockService.saveOrGetStock(stock); // Ensure that stock is saved with ID
        // Add StockAdditionalInfo
        StockAdditionalInfo additionalInfo = new StockAdditionalInfo();
        String uuid = UUID.randomUUID().toString();
        additionalInfo.setId(uuid);
        additionalInfo.setPortfolio(portfolio);
        additionalInfo.setStock(stock);
        additionalInfo.setWhyAdding(whyAdding);
        additionalInfo.setFundamentalCatalysts(fundamentalCatalysts);
        additionalInfo.setRisks(risks);
        additionalInfo.setOtherNotes(otherNotes);

        stockService.addStockAdditionalInfo(additionalInfo);

        // Add Transaction
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setPortfolio(portfolio);
        transaction.setStock(stock);
        transaction.setStockSymbol(symbol);
        transaction.setCompanyName(stock.getName());
        transaction.setPrice(price);
        transaction.setQuantity(quantity);
        transaction.setTransactionType(transactionType);
        transaction.setTotalValue(price * quantity);

        if(transactionType == TransactionType.SELL) {
            if(sellType == SellType.PORTFOLIO) {
                transaction.setSellType(sellType);
            }
        }

        transactionRepo.save(transaction);

        return "redirect:/portfolios"; // Redirect to portfolio details page
    }

    // stock details page
    @GetMapping("/details/{stockId}")
    public String getStockDetails(@PathVariable String stockId, @RequestParam String portfolioId, Model model) {
        // Fetch stock details
        Stock stock = stockService.getStockById(stockId);

        // Fetch transactions for the stock within the portfolio
        List<Transaction> transactions = transactionService.getTransactionsByStockInPortfolio(portfolioId, stockId);

        // Fetch transactions grouped by portfolio
        Map<Portfolio, List<Transaction>> transactionsGroupedByPortfolio = transactionService.getTransactionsGroupedByPortfolioForStock(stockId);

        model.addAttribute("transactions", transactions);
        model.addAttribute("stock", stock);
        model.addAttribute("portfolioId", portfolioId);
        model.addAttribute("transactionsGroupedByPortfolio", transactionsGroupedByPortfolio);

        return "stock/details";
    }
    
    

}
