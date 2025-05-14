package com.portifolio.wealthinker.portfolio.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.portifolio.wealthinker.portfolio.models.Portfolio;
import com.portifolio.wealthinker.portfolio.models.PortfolioHistory;
import com.portifolio.wealthinker.portfolio.models.PortfolioStock;
import com.portifolio.wealthinker.portfolio.models.PortfolioStockHistory;
import com.portifolio.wealthinker.portfolio.models.Stock;
import com.portifolio.wealthinker.portfolio.models.StockAdditionalInfo;
import com.portifolio.wealthinker.portfolio.models.Transaction;
import com.portifolio.wealthinker.portfolio.repositories.PortfolioHistoryRepo;
import com.portifolio.wealthinker.portfolio.repositories.PortfolioStockHistoryRepo;
import com.portifolio.wealthinker.portfolio.repositories.PortfolioStockRepo;
import com.portifolio.wealthinker.portfolio.repositories.TransactionRepo;
import com.portifolio.wealthinker.portfolio.services.PortfolioService;
import com.portifolio.wealthinker.portfolio.services.StockService;
import com.portifolio.wealthinker.portfolio.services.TransactionService;
import com.portifolio.wealthinker.user.models.User;
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

    private final PortfolioStockRepo portfolioStockRepo;

    private final PortfolioHistoryRepo portfolioHistoryRepo;

    private final PortfolioStockHistoryRepo portfolioStockHistoryRepo;

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

        if(transactionType == TransactionType.SELL) {
            if(sellType == SellType.PORTFOLIO) {
                transactionService.sellStockFromPortfolio(stock.getSymbol(), portfolioId, quantity);
            }else{
                transactionService.sellStockFromTotalHoldings(stock.getSymbol(), quantity);
            }
        }
        
        stock.setName(name);
        stock.setSymbol(symbol);
        // stock.setPortfolio(portfolio);
        stock.setMarketPrice(marketPrice);

        // System.out.println("Stock Details" + stock.getId() + " " + stock.getSymbol() + " " + stock.getName() + " " + stock.getPortfolio());

        
        

        stock = stockService.saveOrGetStock(stock); // Ensure that stock is saved with ID
        // Add StockAdditionalInfo

        PortfolioStock portfolioStock = portfolioStockRepo.findByPortfolioAndStock(portfolio, stock).orElse(null);


        if (portfolioStock == null) {
            portfolioStock = new PortfolioStock();
            portfolioStock.setId(UUID.randomUUID().toString());
            portfolioStock.setPortfolio(portfolio);
            portfolioStock.setStock(stock);
            portfolioStock.setQuantity(quantity);
            portfolioStock.setTotalInvestedValue(price * quantity);
            portfolioStock.setTotalCurrentValue(marketPrice * quantity);
            portfolioStock.setAveragePrice(price);
            portfolioStock.setUnrealizedProfitLoss(marketPrice * quantity - price * quantity);
            portfolio.setTotalInvestedValue(portfolio.getTotalInvestedValue() + price * quantity);
            portfolio.setTotalCurrentValue(portfolio.getTotalCurrentValue() + marketPrice * quantity);
            portfolio.setUnrealizedProfitLoss(portfolio.getUnrealizedProfitLoss() + portfolioStock.getUnrealizedProfitLoss());
        }else{

            double prevCurrent = portfolioStock.getTotalCurrentValue();
            double prevInvested = portfolioStock.getTotalInvestedValue();
            double prevRealized = portfolioStock.getRealizedProfitLoss();
            double prevUnrealized = portfolioStock.getUnrealizedProfitLoss();


            if(transactionType == TransactionType.BUY){
                int newQty = portfolioStock.getQuantity() + quantity;
                double newInvested = prevInvested + price * quantity;
                double newAvg = newInvested / newQty;

                portfolioStock.setQuantity(newQty);
                portfolioStock.setTotalInvestedValue(newInvested);
                portfolioStock.setAveragePrice(newAvg);
                // portfolioStock.setUnrealizedProfitLoss(marketPrice * portfolioStock.getQuantity() - portfolioStock.getAveragePrice() * portfolioStock.getQuantity());
                // portfolio.setUnrealizedProfitLoss(portfolio.getUnrealizedProfitLoss() + portfolioStock.getUnrealizedProfitLoss());
                // portfolio.setTotalCurrentValue(portfolio.getTotalCurrentValue() + marketPrice * quantity);

            }else{
                int newQty = portfolioStock.getQuantity() - quantity;
                portfolioStock.setQuantity(newQty);

                double realized = (price - portfolioStock.getAveragePrice()) * quantity;
                portfolioStock.setRealizedProfitLoss(prevRealized + realized);

                double newInvested = prevInvested - portfolioStock.getAveragePrice() * quantity;
                portfolioStock.setTotalInvestedValue(newInvested);

                portfolio.setRealizedProfitLoss(portfolio.getRealizedProfitLoss() + realized);
                // portfolio.setTotalCurrentValue(portfolio.getTotalCurrentValue() - marketPrice * quantity);

            }
            double newCurrent = marketPrice * portfolioStock.getQuantity();
            double newUnrealized = newCurrent - portfolioStock.getTotalInvestedValue();

            portfolioStock.setTotalCurrentValue(newCurrent);
            portfolioStock.setUnrealizedProfitLoss(newUnrealized);

            portfolio.setTotalInvestedValue(portfolio.getTotalInvestedValue() - prevInvested + portfolioStock.getTotalInvestedValue());

            portfolio.setTotalCurrentValue(portfolio.getTotalCurrentValue() - prevCurrent + newCurrent);

            portfolio.setUnrealizedProfitLoss(portfolio.getUnrealizedProfitLoss() - prevUnrealized + newUnrealized);
        }
        // portfolioStock.setCurrentPrice(marketPrice);
        // portfolioStock.setTotalCurrentValue(marketPrice * portfolioStock.getQuantity());
        portfolioStockRepo.save(portfolioStock);

        StockAdditionalInfo additionalInfo = new StockAdditionalInfo();
        String uuid = UUID.randomUUID().toString();
        additionalInfo.setId(uuid);
        additionalInfo.setPortfolio(portfolio);
        additionalInfo.setStock(stock);
        additionalInfo.setWhyAdding(whyAdding);
        additionalInfo.setFundamentalCatalysts(fundamentalCatalysts);
        additionalInfo.setRisks(risks);
        additionalInfo.setOtherNotes(otherNotes);

        additionalInfo.setPortfolioStock(portfolioStock);

        stockService.addStockAdditionalInfo(additionalInfo);

        // Add Transaction
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setPortfolio(portfolio);
        transaction.setStock(stock);
        transaction.setUserId(userId);
        transaction.setStockSymbol(symbol);
        transaction.setCompanyName(stock.getName());
        transaction.setPrice(price);
        transaction.setQuantity(quantity);
        transaction.setTransactionType(transactionType);
        transaction.setTotalValue(price * quantity);

        transaction.setPortfolioStock(portfolioStock);

        if(transactionType == TransactionType.SELL) {
            if(sellType == SellType.PORTFOLIO) {
                transaction.setSellType(sellType);
            }
            portfolio.setTotalInvestedValue(portfolio.getTotalInvestedValue() - transaction.getTotalValue());
        }else{
            portfolio.setTotalInvestedValue(portfolio.getTotalInvestedValue() + transaction.getTotalValue());
        }

        transactionRepo.save(transaction);
        

        PortfolioHistory portfolioHistory = new PortfolioHistory();
        portfolioHistory.setId(UUID.randomUUID().toString());
        portfolioHistory.setPortfolio(portfolio);
        portfolioHistory.setTotalCurrentValue(portfolio.getTotalCurrentValue());
        portfolioHistory.setTotalInvestedValue(portfolio.getTotalInvestedValue());
        portfolioHistory.setNetGains(price);

        portfolioHistoryRepo.save(portfolioHistory);


        PortfolioStockHistory portfolioStockHistory = new PortfolioStockHistory();
        portfolioStockHistory.setId(UUID.randomUUID().toString());
        portfolioStockHistory.setQuantity(portfolioStock.getQuantity());
        portfolioStockHistory.setPortfolioStock(portfolioStock);
        portfolioStockHistory.setCurrentPrice(marketPrice);
        portfolioStockHistory.setAveragePrice(portfolioStock.getAveragePrice());
        portfolioStockHistory.setTotalCurrentValue(portfolioStock.getTotalInvestedValue());

        portfolioStockHistoryRepo.save(portfolioStockHistory);


        return "redirect:/portfolios"; // Redirect to portfolio details page
    }

    // stock details page
    @GetMapping("/details/{stockId}")
    public String getStockDetails(@PathVariable String stockId, @RequestParam String portfolioId, @ModelAttribute("loggedInUser") User loggedInUser, Model model) {
        // Fetch stock details
        Stock stock = stockService.getStockById(stockId);

        // Fetch transactions for the stock within the portfolio
        List<Transaction> transactions = transactionService.getTransactionsByStockInPortfolio(portfolioId, stockId);

        // Fetch transactions grouped by portfolio
        Map<Portfolio, List<Transaction>> transactionsGroupedByPortfolio = transactionService.getTransactionsGroupedByPortfolioForStock(stockId, loggedInUser);

        model.addAttribute("transactions", transactions);
        model.addAttribute("stock", stock);
        model.addAttribute("portfolioId", portfolioId);
        model.addAttribute("transactionsGroupedByPortfolio", transactionsGroupedByPortfolio);

        return "stock/details";
    }
    
    

}
