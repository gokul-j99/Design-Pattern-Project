package com.stock.management.controllers;

import com.stock.management.commands.Command;
import com.stock.management.commands.TransactionManager;
import com.stock.management.facade.StockMarketFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionManager transactionManager = new TransactionManager();
    private final StockMarketFacade stockMarketFacade = new StockMarketFacade();

    @PostMapping("/buy")
    public ResponseEntity<String> buyStock(
            @RequestParam String username,
            @RequestParam String stockName,
            @RequestParam String portfolioName,
            @RequestParam int quantity,
            @RequestParam String currencyType) {
        try {
            String result = stockMarketFacade.buyStock(username, stockName, portfolioName, quantity, currencyType);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/undo")
    public ResponseEntity<String> undoLastTransaction(@RequestParam String username) {
        try {
            transactionManager.undoLastCommand(username);
            return ResponseEntity.ok("Last transaction undone.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/sell")
    public ResponseEntity<String> sellStock(
            @RequestParam String username,
            @RequestParam String stockName,
            @RequestParam String portfolioName,
            @RequestParam int quantity,
            @RequestParam String currencyType) {
        try {
            String result = stockMarketFacade.sellStock(username, stockName, portfolioName, quantity, currencyType);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/details")
    public ResponseEntity<String> getStockDetails(@RequestParam String stockName) {
        try {
            String result = stockMarketFacade.checkStockDetails(stockName);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<Object> getHistory(@RequestParam String username) {
        try {
            List<Command> history = transactionManager.viewTransactionHistory(username);
            if (history == null || history.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is no transaction for this user");
        }
    }
}
