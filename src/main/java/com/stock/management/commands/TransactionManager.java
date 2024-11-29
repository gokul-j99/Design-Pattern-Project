package com.stock.management.commands;

import com.stock.management.storage.InMemoryDatabase;

public class TransactionManager {

    public void executeCommand(Command command) {
        command.execute();
        InMemoryDatabase.addTransaction(command); // Store command in the database
    }

    public void undoLastCommand() {
        Command lastCommand = InMemoryDatabase.getLastTransaction();
        if (lastCommand != null) {
            lastCommand.undo();
        } else {
            System.out.println("No commands to undo.");
        }
    }

    public void viewTransactionHistory() {
        for (Command command : InMemoryDatabase.getTransactionHistory()) {
            System.out.println(command.toString());
        }
    }
}
