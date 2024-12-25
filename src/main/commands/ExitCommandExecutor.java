package main.commands;

import main.OutputPrinter;
import main.exceptions.InvalidCommandException;
import main.models.Command;
import main.services.ExpenseService;

public class ExitCommandExecutor extends CommandExecutor{
    public static String COMMAND_NAME = "exit";

    public ExitCommandExecutor(OutputPrinter outputPrinter, ExpenseService expenseService) {
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
        outputPrinter.exit();
    }
}
