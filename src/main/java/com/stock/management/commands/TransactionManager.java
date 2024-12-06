package com.stock.management.commands;

import com.stock.management.storage.InMemoryDatabase;

import java.util.List;

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

    public List<Command> viewTransactionHistory(String userName) {
        for (Command command : InMemoryDatabase.getTransactionHistory(userName)) {
            System.out.println(command.toString());
        }
        return InMemoryDatabase.getTransactionHistory(userName).stream().toList();
    }
}
