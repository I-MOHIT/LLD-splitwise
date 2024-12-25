package main.commands;

import main.OutputPrinter;
import main.exceptions.InvalidCommandException;
import main.models.Command;
import main.models.Group;
import main.models.User;
import main.services.ExpenseService;

import java.util.HashMap;

public class CreateGroupCommandExecutor extends CommandExecutor{
    public static String COMMAND_NAME = "create_group";

    public CreateGroupCommandExecutor(OutputPrinter outputPrinter, ExpenseService expenseService) {
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
        Group group = new Group(command.getParams().getFirst());
        expenseService.getUuidGroupMap().put(group.getGroupId(), group);
        expenseService.getGroupedUserToUserBalances().put(group.getGroupId().toString(), new HashMap<>());
        outputPrinter.createGroup(group);
    }
}
