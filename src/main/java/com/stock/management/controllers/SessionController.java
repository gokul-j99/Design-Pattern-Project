package com.stock.management.controllers;

import com.stock.management.session.Session;
import com.stock.management.storage.InMemoryDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    @Autowired
    private Session session;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String role) {
        session.login(username, role);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Logged in as: " + username + " with role: " + role);
    }

    @GetMapping("/user")
    public ResponseEntity<String> getLoggedInUser() {
        String loggedInUser = session.getLoggedInUser();
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No user is currently logged in.");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body("Current user: " + loggedInUser);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String username) {
        boolean success = session.logout(username);
        if (success) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Logged out successfully: " + username);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No active session found for username: " + username);
        }
    }

    @GetMapping("/userdetails")
    public ResponseEntity<Object> userDetails(@RequestParam String username) {
        if(!InMemoryDatabase.isValidUser(username)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No active session found for username: " + username);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(InMemoryDatabase.getPortfolios(username));
    }
}
