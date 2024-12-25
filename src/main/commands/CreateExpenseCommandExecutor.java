package main.commands;

import main.OutputPrinter;
import main.exceptions.GroupNotFoundException;
import main.exceptions.InvalidCommandException;
import main.models.*;
import main.services.ExpenseService;
import main.validators.EqualSplitValidator;
import main.validators.ExactSplitValidator;
import main.validators.PercentSplitValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateExpenseCommandExecutor extends CommandExecutor{
    public static String COMMAND_NAME = "create_expense";

    public CreateExpenseCommandExecutor(OutputPrinter outputPrinter, ExpenseService expenseService) {
        super(outputPrinter, expenseService);
    }

    @Override
    public boolean validateCommand(Command command) {
        if (!command.getCommandName().equals(COMMAND_NAME) || command.getParams().size() < 6) {
            throw new InvalidCommandException();
        }
        return true;
    }

    @Override
    public void executeCommand(Command command) {
        String groupIdString = command.getParams().getFirst();
        if (!groupIdString.equals("null") && !this.expenseService.getUuidGroupMap().containsKey(UUID.fromString(groupIdString))) {
            throw new GroupNotFoundException();
        }
        double expenseAmount = Double.parseDouble(command.getParams().get(1));
        UUID paidByUserId = UUID.fromString(command.getParams().get(2));
        int numUsers = Integer.parseInt(command.getParams().get(3));
        SplitType splitType = SplitType.valueOf(command.getParams().get(4 + numUsers));
        List<Split> splitList = new ArrayList<>();
        Expense expense = new Expense();
        switch (splitType) {
            case EQUAL:
                for (int i = 0; i < numUsers; i++) {
                    Split split = new Split(UUID.fromString(command.getParams().get(4 + i)), 0, SplitType.EQUAL);
                    splitList.add(split);
                }
                expense = expenseService.addExpense(new EqualSplitValidator(splitList), paidByUserId, expenseAmount, splitList, SplitType.EQUAL, new ExpenseMetadata("Equal Type expense", null), groupIdString);
                break;
            case EXACT:
                for (int i = 0; i < numUsers; i++) {
                    Split split = new Split(UUID.fromString(command.getParams().get(4 + i)), Double.parseDouble(command.getParams().get(4 + numUsers + i)), SplitType.EXACT);
                    splitList.add(split);
                }
                expense = expenseService.addExpense(new ExactSplitValidator(splitList), paidByUserId, expenseAmount, splitList, SplitType.EXACT, new ExpenseMetadata("Exact Type expense", null), groupIdString);
                break;
            case PERCENT:
                for (int i = 0; i < numUsers; i++) {
                    Split split = new Split(UUID.fromString(command.getParams().get(4 + i)), Double.parseDouble(command.getParams().get(4 + numUsers + i)), SplitType.PERCENT);
                    splitList.add(split);
                }
                expense = expenseService.addExpense(new PercentSplitValidator(splitList), paidByUserId, expenseAmount, splitList, SplitType.PERCENT, new ExpenseMetadata("Percent Type expense", null), groupIdString);
                break;
            default:
                break;
        }
        User paidByUser = this.expenseService.getUuidUserMap().get(paidByUserId);
        List<UUID> expenseList = paidByUser.getExpenseList();
        if (!expenseList.contains(expense.getExpenseId())) {
            expenseList.add(expense.getExpenseId());
            paidByUser.setExpenseList(expenseList);
            this.expenseService.getUuidUserMap().put(paidByUserId, paidByUser);
        }
        for (int i = 0; i < numUsers; i++) {
            User paidToUser = this.expenseService.getUuidUserMap().get(UUID.fromString(command.getParams().get(4 + i)));
            List<UUID> expenseListPaidTo = paidToUser.getExpenseList();
            if (!expenseListPaidTo.contains(expense.getExpenseId())) {
                expenseListPaidTo.add(expense.getExpenseId());
                paidToUser.setExpenseList(expenseListPaidTo);
                this.expenseService.getUuidUserMap().put(paidByUser.getUserId(), paidByUser);
            }
        }
        outputPrinter.createExpense(expenseService, expense);
    }
}
