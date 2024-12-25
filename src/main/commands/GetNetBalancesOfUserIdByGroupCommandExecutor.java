package main.commands;

import main.OutputPrinter;
import main.exceptions.GroupNotFoundException;
import main.exceptions.InvalidCommandException;
import main.exceptions.UserNotFoundException;
import main.models.Command;
import main.models.Group;
import main.models.User;
import main.services.ExpenseService;

import java.util.UUID;

public class GetNetBalancesOfUserIdByGroupCommandExecutor extends CommandExecutor{
    public static String COMMAND_NAME = "get_net_balances_of_user_id_by_group";

    public GetNetBalancesOfUserIdByGroupCommandExecutor(OutputPrinter outputPrinter, ExpenseService expenseService) {
        super(outputPrinter, expenseService);
    }

    @Override
    public boolean validateCommand(Command command) {
        if (!command.getCommandName().equals(COMMAND_NAME) || command.getParams().size() != 2) {
            throw new InvalidCommandException();
        }
        return true;
    }

    @Override
    public void executeCommand(Command command) {
        UUID userId = UUID.fromString(command.getParams().getFirst());
        if (!this.expenseService.getUuidUserMap().containsKey(userId)) {
            throw new UserNotFoundException();
        }
        UUID groupId = UUID.fromString(command.getParams().get(1));
        if (!this.expenseService.getGroupedUserToUserBalances().containsKey(groupId.toString())) {
            throw new GroupNotFoundException();
        }
        User user = this.expenseService.getUuidUserMap().get(userId);
        Group group = this.expenseService.getUuidGroupMap().get(groupId);
        expenseService.getNetBalancesOfUserIdByGroup(userId, groupId);
    }
}
