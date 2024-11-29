package com.stock.management.session;

import com.stock.management.storage.InMemoryDatabase;
import org.springframework.stereotype.Component;

@Component
public class Session {
    private String loggedInUser;
    private String role;

    public String getLoggedInUser() {
        return loggedInUser;
    }

    public void login(String user, String role) {
        InMemoryDatabase.saveSession(user,role);
        this.loggedInUser = user;
        this.role  =role;
    }

    public  void  logout(String user){
        InMemoryDatabase.removeSession(user);
        this.loggedInUser= "";
        this.role = "";
    }

}
