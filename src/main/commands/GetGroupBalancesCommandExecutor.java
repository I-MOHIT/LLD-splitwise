package main.commands;

import main.OutputPrinter;
import main.exceptions.GroupNotFoundException;
import main.exceptions.InvalidCommandException;
import main.models.Command;
import main.models.Group;
import main.services.ExpenseService;

import java.util.UUID;

public class GetGroupBalancesCommandExecutor extends CommandExecutor{
    public static String COMMAND_NAME = "get_group_balances";

    public GetGroupBalancesCommandExecutor(OutputPrinter outputPrinter, ExpenseService expenseService) {
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
        UUID groupId = UUID.fromString(command.getParams().getFirst());
        if (!this.expenseService.getGroupedUserToUserBalances().containsKey(groupId.toString())) {
            throw new GroupNotFoundException();
        }
        Group group = this.expenseService.getUuidGroupMap().get(groupId);
        outputPrinter.getGroupBalances(group);
        expenseService.getNetBalancesByGroup(groupId);
    }
}
