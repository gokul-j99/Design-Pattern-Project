package com.stock.management.controllers;

import com.stock.management.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    @Autowired
    private Session session;

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String Role) {
        session.login(username,Role);
        return "Logged in as: " + username;
    }

    @GetMapping("/user")
    public String getLoggedInUser() {
        return "Current user: " + session.getLoggedInUser();
    }

    @DeleteMapping("logout")
    public String logout(@RequestParam String username){
        session.logout(username);
        return "Logout Successfully";
    }
}
