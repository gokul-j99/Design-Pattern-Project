package com.stock.management.controllers;

import com.stock.management.commands.TransactionManager;
import com.stock.management.facade.StockMarketFacade;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private TransactionManager transactionManager = new TransactionManager();

    private StockMarketFacade stockMarketFacade = new StockMarketFacade();;

    @PostMapping("/buy")
    public String buyStock(
            @RequestParam String username,
            @RequestParam String stockName,
            @RequestParam String portfolioName,
            @RequestParam int quantity) {

        return stockMarketFacade.buyStock(username, stockName, portfolioName, quantity);
    }

    @PostMapping("/undo")
    public String undoLastTransaction(@RequestParam String username) {
        transactionManager.undoLastCommand(username);
        return "Last transaction undone.";
    }


    @PostMapping("/sell")
    public String sellStock(
            @RequestParam String username,
            @RequestParam String stockName,
            @RequestParam String portfolioName,
            @RequestParam int quantity) {
        // Validate portfolio
        return stockMarketFacade.sellStock(username, stockName, portfolioName, quantity);

    }

    @GetMapping("/details")
    public String getStockDetails(@RequestParam String stockName) {
        return stockMarketFacade.checkStockDetails(stockName);
    }
}
