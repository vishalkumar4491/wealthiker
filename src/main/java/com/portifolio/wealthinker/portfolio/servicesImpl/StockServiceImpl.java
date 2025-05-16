package com.portifolio.wealthinker.portfolio.servicesImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portifolio.wealthinker.exceptions.ResourceNotFoundException;
import com.portifolio.wealthinker.portfolio.models.Stock;
import com.portifolio.wealthinker.portfolio.models.StockAdditionalInfo;
import com.portifolio.wealthinker.portfolio.repositories.StockAdditionalInfoRepo;
import com.portifolio.wealthinker.portfolio.repositories.StockRepo;
import com.portifolio.wealthinker.portfolio.services.StockService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepo stockRepo;

    private final StockAdditionalInfoRepo stockAdditionalInfoRepo;

    private final WebClient webClient;

    @Override
    public Stock getStockById(String id) {
        return stockRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Stock not found"));
    }
   
    @Override
    public List<Stock> searchStocks(String keyword) {
        // Call Alpha Vantage API and parse results
        String apiUrl = "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords=" + keyword + "&apikey=FO3N3NMLOALAHIPA";

         // Make an asynchronous request using WebClient
        String response = webClient.get().uri(apiUrl).
                            retrieve().bodyToMono(String.class).
                            block();   // blocking for simplicity, consider using reactive for non-blocking
        System.out.println("API Response: " + response);
        // Parse the response and return a list of Stock objects
        return parseStockApiResponse(response);
    }

    private List<Stock> parseStockApiResponse(String response) {
        // Parse the response and return a list of Stock objects
        // Convert JSON response to Stock objects
        // Example: Use ObjectMapper (Jackson) for parsing
        System.out.println("API Response: " + response);
        ObjectMapper objectMapper = new ObjectMapper();
        List<Stock> stockList = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode bestMatchesNode = rootNode.get("bestMatches");

            if(bestMatchesNode != null){
                for (JsonNode matchNode : bestMatchesNode) {
                    Stock stock = new Stock();
                    stock.setSymbol(matchNode.get("1. symbol").asText());
                    stock.setName(matchNode.get("2. name").asText());
                    stock.setSector("N/A"); // Sector info may not be available
                    stockList.add(stock);
                }
            }else{
                System.out.println("No matches found.");
                System.out.println("Error parsing the response.");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Parsed Stocks: " + stockList);
        return stockList;
    }

    // Ids E1344LXX21SVZCAU, S753VMJ9O92MMVV2, 8ATIEHYC19AS4AAW, FO3N3NMLOALAHIPA



    @Override
    public Stock getStockDetails(String symbol) {
        String apiUrl = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + symbol + "&outputsize=compact&apikey=S753VMJ9O92MMVV2";

        // Make an asynchronous request using WebClient
        String response = webClient.get().uri(apiUrl).
                            retrieve().bodyToMono(String.class).
                            block();   // blocking for simplicity, consider using reactive for non-blocking
        System.out.println("API Stock Response: " + response);
        return parseStockDetailResponse(response);
    }

    private Stock parseStockDetailResponse(String responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        Stock stock = new Stock();
        try {
            // Parse the JSON response
            JsonNode rootNode = objectMapper.readTree(responseBody);

            // Extract metadata for symbol and other details
            JsonNode metaData = rootNode.path("Meta Data");
            String symbol = metaData.path("2. Symbol").asText();
            String lastRefreshed = metaData.path("3. Last Refreshed").asText();
            
            // Extract the 'Time Series (Daily)' node
            JsonNode timeSeries = rootNode.path("Time Series (Daily)");

            // Get the latest date's data
            JsonNode latestData = timeSeries.path(lastRefreshed);

            // Extract required fields (e.g., closing price)
            double closingPrice = latestData.path("4. close").asDouble();

            System.out.println("Current Price "  + closingPrice);
            
            // Set values in the stock object
            stock.setSymbol(symbol); // Symbol from metadata
            stock.setName("StockName"); // Placeholder, replace with actual stock name if available
            stock.setMarketPrice(closingPrice); // Latest closing price

        } catch (IOException e) {
            e.printStackTrace();
            // Handle errors, such as API limit exceeded or invalid JSON format
        }
    return stock;
    }

    @Override
    public void addStockAdditionalInfo(StockAdditionalInfo info) {
        stockAdditionalInfoRepo.save( info);
    }

    @Override
    @Transactional
    public Stock saveOrGetStock(Stock stock) {
        Optional<Stock> existingStock = stockRepo.findBySymbol(stock.getSymbol());
        return existingStock.orElseGet(() -> stockRepo.save(stock));
    }

    @Override
    public Stock getStockBySymbol(String symbol) {
        return stockRepo.findBySymbol(symbol).orElse(null);
    }
    

}
