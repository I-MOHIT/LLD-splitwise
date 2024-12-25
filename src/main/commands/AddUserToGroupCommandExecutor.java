package main.commands;

import main.OutputPrinter;
import main.exceptions.GroupNotFoundException;
import main.exceptions.InvalidCommandException;
import main.exceptions.UserNotFoundException;
import main.models.Command;
import main.models.Group;
import main.models.User;
import main.services.ExpenseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddUserToGroupCommandExecutor extends CommandExecutor{
    public static String COMMAND_NAME = "add_user_to_group";

    public AddUserToGroupCommandExecutor(OutputPrinter outputPrinter, ExpenseService expenseService) {
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
        if (!this.expenseService.getUuidGroupMap().containsKey(groupId)) {
            throw new GroupNotFoundException();
        }
        User user = this.expenseService.getUuidUserMap().get(userId);
        Group group = this.expenseService.getUuidGroupMap().get(groupId);

        List<UUID> groupIdList = user.getGroupIdList();
        groupIdList.add(groupId);
        user.setGroupIdList(groupIdList);
        this.expenseService.getUuidUserMap().put(userId, user);

        List<UUID> userIdList = group.getUserList();
        userIdList.add(userId);
        group.setUserList(userIdList);
        this.expenseService.getUuidGroupMap().put(groupId, group);

        Map<UUID, Map<UUID, Double>> userToUserBalances = this.expenseService.getGroupedUserToUserBalances().get(groupId.toString());
        userToUserBalances.put(userId, new HashMap<>());
        this.expenseService.getGroupedUserToUserBalances().put(groupId.toString(), userToUserBalances);
        outputPrinter.addUserToGroup(user, group);
    }
}
