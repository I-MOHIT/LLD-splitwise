package main.commands;

import main.OutputPrinter;
import main.exceptions.ExpenseNotFoundException;
import main.exceptions.InvalidCommandException;
import main.models.Command;
import main.models.Expense;
import main.services.ExpenseService;

import java.util.UUID;

public class GetExpenseByIdCommandExecutor extends CommandExecutor{
    public static String COMMAND_NAME = "get_expense_by_id";

    public GetExpenseByIdCommandExecutor(OutputPrinter outputPrinter, ExpenseService expenseService) {
        super(outputPrinter, expenseService);
    }

    @Override
    public boolean validateCommand(Command command) {
        if (!command.getCommandName().equals(COMMAND_NAME) || command.getParams().size() != 1) {
            throw new InvalidCommandException();
        }
        return true;
    }

    @Override
    public void executeCommand(Command command) {
        UUID expenseId = UUID.fromString(command.getParams().getFirst());
        if (!this.expenseService.getExpenseMap().containsKey(expenseId)) {
            throw new ExpenseNotFoundException();
        }
        Expense expense = expenseService.getExpenseMap().get(expenseId);
        outputPrinter.getExpense(expenseService, expense);
    }
}
