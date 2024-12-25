package main;

import main.commands.CommandExecutorFactory;
import main.services.ExpenseService;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        OutputPrinter outputPrinter = new OutputPrinter();
        ExpenseService expenseService = new ExpenseService();

        CommandExecutorFactory commandExecutorFactory = new CommandExecutorFactory(expenseService, outputPrinter);

        new InteractiveMode(commandExecutorFactory, outputPrinter).process();
    }
}