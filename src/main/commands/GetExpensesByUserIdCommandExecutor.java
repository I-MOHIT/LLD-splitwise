package main.commands;

import main.OutputPrinter;
import main.exceptions.InvalidCommandException;
import main.exceptions.UserNotFoundException;
import main.models.Command;
import main.models.Expense;
import main.models.Split;
import main.models.User;
import main.services.ExpenseService;

import java.util.List;
import java.util.UUID;

public class GetExpensesByUserIdCommandExecutor extends CommandExecutor{
    public static String COMMAND_NAME = "get_expenses_by_user_id";

    public GetExpensesByUserIdCommandExecutor(OutputPrinter outputPrinter, ExpenseService expenseService) {
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
        UUID userId = UUID.fromString(command.getParams().getFirst());
        if (!this.expenseService.getUuidUserMap().containsKey(userId)) {
            throw new UserNotFoundException();
        }
        User user = this.expenseService.getUuidUserMap().get(userId);
        List<UUID> expenseList = user.getExpenseList();
        outputPrinter.getExpensesByUserId(user);
        for (UUID expenseId : expenseList) {
            Expense expense = this.expenseService.getExpenseMap().get(expenseId);
            System.out.println("Amount - " + expense.getExpenseAmount() + " , Paid By - " + this.expenseService.getUuidUserMap().get(expense.getPaidByUserId()).getName() + " , Users involved and their respective shares -");
            for (Split split : expense.getSplitList()) {
                System.out.println(this.expenseService.getUuidUserMap().get(split.getSplitUser()).getName() + " " + split.getShareAmount());
            }
        }
    }
}
