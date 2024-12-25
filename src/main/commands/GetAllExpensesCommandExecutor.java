package main.commands;

import main.OutputPrinter;
import main.exceptions.InvalidCommandException;
import main.models.Command;
import main.models.Expense;
import main.services.ExpenseService;

import java.util.UUID;

public class GetAllExpensesCommandExecutor extends CommandExecutor{
    public static String COMMAND_NAME = "get_all_expenses";

    public GetAllExpensesCommandExecutor(OutputPrinter outputPrinter, ExpenseService expenseService) {
        super(outputPrinter, expenseService);
    }

    @Override
    public boolean validateCommand(Command command) {
        if (!command.getCommandName().equals(COMMAND_NAME) || !command.getParams().isEmpty()) {
            throw new InvalidCommandException();
        }
        return true;
    }

    @Override
    public void executeCommand(Command command) {
        outputPrinter.getAllExpenses(expenseService);
    }
}
