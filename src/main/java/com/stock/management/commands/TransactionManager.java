package com.stock.management.commands;

import com.stock.management.storage.InMemoryDatabase;

public class TransactionManager {

    public void executeCommand(String userName,Command command) {
        command.execute();
        InMemoryDatabase.addTransaction(userName,command); // Store command in the database
    }

    public void undoLastCommand(String userName) {
        Command lastCommand = InMemoryDatabase.getLastTransaction(userName);
        if (lastCommand != null) {
            lastCommand.undo();
        } else {
            System.out.println("No commands to undo.");
        }
    }

    public void viewTransactionHistory(String userName) {
        for (Command command : InMemoryDatabase.getTransactionHistory(userName)) {
            System.out.println(command.toString());
        }
    }
}
