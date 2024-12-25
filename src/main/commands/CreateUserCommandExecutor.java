package main.commands;

import main.OutputPrinter;
import main.exceptions.InvalidCommandException;
import main.models.Command;
import main.models.User;
import main.services.ExpenseService;

public class CreateUserCommandExecutor extends CommandExecutor{
    public static String COMMAND_NAME = "create_user";

    public CreateUserCommandExecutor(OutputPrinter outputPrinter, ExpenseService expenseService) {
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
        User user = new User(command.getParams().getFirst());
        expenseService.addUsersToHashMap(user);
        outputPrinter.createUser(user);
    }
}
