package com.stock.management.controllers;

import com.stock.management.models.Portfolio;
import com.stock.management.storage.InMemoryDatabase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    @PostMapping("/create")
    public ResponseEntity<String> createPortfolio(@RequestParam String username, @RequestParam String portfolioName) {
        String role = InMemoryDatabase.getUserRole(username);
        if (role == null || !role.equals("user")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Permission denied. Only users can create portfolios.");
        }

        Portfolio portfolio = new Portfolio.PortfolioBuilder()
                .setName(portfolioName)
                .setOwner(username)
                .build();
        InMemoryDatabase.savePortfolio(username, portfolio);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Portfolio '" + portfolioName + "' created for user: " + username);
    }


    @GetMapping("/view")
    public ResponseEntity<Object> fetchPortfolio( @RequestParam String username){
       Set<Portfolio> portfolioList =  InMemoryDatabase.getPortfolios(username);
        return ResponseEntity.status(HttpStatus.OK).body(portfolioList);


    }

   @DeleteMapping("/deletePortfolio")
    public ResponseEntity<String> deletePortfolio(@RequestParam String username, @RequestParam String portfolioName ){
       String role = InMemoryDatabase.getUserRole(username);
        if (role == null || !role.equals("user")) {
           return ResponseEntity.status(HttpStatus.FORBIDDEN)
                   .body("Permission denied. Only users can create portfolios.");
       }
        if(! InMemoryDatabase.deletePortfolio(username,portfolioName)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The portfolio '" + portfolioName + "' is not in the database for the given user. Please add the portfolio.");
        }
       return ResponseEntity.status(HttpStatus.OK).body("Deleted Successfully");
   }
}
