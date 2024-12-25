package main.commands;

import main.OutputPrinter;
import main.exceptions.GroupNotFoundException;
import main.exceptions.InvalidCommandException;
import main.exceptions.UserNotFoundException;
import main.models.*;
import main.services.ExpenseService;

import java.util.List;
import java.util.UUID;

public class GetExpensesOfUserIdByGroupCommandExecutor extends CommandExecutor{
    public static String COMMAND_NAME = "get_expenses_of_user_id_by_group";

    public GetExpensesOfUserIdByGroupCommandExecutor(OutputPrinter outputPrinter, ExpenseService expenseService) {
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
        List<UUID> expenseList = user.getExpenseList();
        outputPrinter.getExpensesOfUserIdByGroup(user, group);
        for (UUID expenseId : expenseList) {
            Expense expense = this.expenseService.getExpenseMap().get(expenseId);
            if (expense.getGroupId().equals(groupId.toString())) {
                System.out.println("Amount - " + expense.getExpenseAmount() + " , Paid By - " + this.expenseService.getUuidUserMap().get(expense.getPaidByUserId()).getName() + " , Users involved and their respective shares -");
                for (Split split : expense.getSplitList()) {
                    System.out.println(this.expenseService.getUuidUserMap().get(split.getSplitUser()).getName() + " " + split.getShareAmount());
                }
            }
        }
    }
}
