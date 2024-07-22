package com.example.demo.controller;


import com.example.demo.model.Stock;
import com.example.demo.model.Portfolio;
import com.example.demo.repository.StockRepository;
import com.example.demo.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StockController {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @PostMapping("/buy")
    public void buyStock(@RequestBody Stock stock) {
        Stock existingStock = stockRepository.findByStockName(stock.getStockName());
        if (existingStock != null) {
            existingStock.setQuantity(existingStock.getQuantity() + stock.getQuantity());
            existingStock.setPrice(stock.getPrice());
            stockRepository.save(existingStock);
        } else {
            stockRepository.save(stock);
        }

        // Update portfolio
        Portfolio portfolio = portfolioRepository.findById(stock.getId()).orElse(new Portfolio());
        portfolio.setStockName(stock.getStockName());
        portfolio.setQuantity(stock.getQuantity());
        portfolio.setPrice(stock.getPrice());
        portfolioRepository.save(portfolio);
    }

    /**
     * @param stock
     */
    @PostMapping("/sell")
    public void sellStock(@RequestBody Stock stock) {
        Stock existingStock = stockRepository.findByStockName(stock.getStockName());
        if (existingStock != null && existingStock.getQuantity() >= stock.getQuantity()) {
            existingStock.setQuantity(existingStock.getQuantity() - stock.getQuantity());
            stockRepository.save(existingStock);

            // Remove from portfolio if quantity is zero
            if (existingStock.getQuantity() == 0) {
                stockRepository.delete(existingStock);
            }
        } else {
            throw new RuntimeException("Not enough stock to sell");
        }
    }

    @GetMapping("/portfolio")
    public List<Portfolio> getPortfolio() {
        return portfolioRepository.findAll();
    }

    @GetMapping("/performance")
    public double getPerformance() {
        return portfolioRepository.findAll().stream()
                .mapToDouble(p -> p.getQuantity() * p.getPrice())
                .sum();
    }
}
