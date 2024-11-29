package com.stock.management.commands;

public interface Command {
    void execute();
    void undo();
}
