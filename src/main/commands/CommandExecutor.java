package main.commands;

import main.OutputPrinter;
import main.models.Command;
import main.services.ExpenseService;

public abstract class CommandExecutor {
    OutputPrinter outputPrinter;
    ExpenseService expenseService;

    public CommandExecutor(OutputPrinter outputPrinter, ExpenseService expenseService) {
        this.outputPrinter = outputPrinter;
        this.expenseService = expenseService;
    }

    public abstract boolean validateCommand(Command command);

    public abstract void executeCommand(Command command);
}
