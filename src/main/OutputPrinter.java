package main;

import main.models.Expense;
import main.models.Group;
import main.models.User;
import main.services.ExpenseService;

import java.util.Map;
import java.util.UUID;

public class OutputPrinter {
    public void hello() {
        System.out.println("Hello!");
    }

    public void exit() {
        System.out.println("Bye!");
    }

    public void createUser(User user) {
        System.out.println("Created user named " + user.getName() + " having user id " + user.getUserId());
    }

    public void getGroupBalances(Group group) {
        System.out.println("The balances for all the users in the group " + group.getName() + " are listed below -");
    }

    public void createExpense(ExpenseService expenseService, Expense expense) {
        System.out.println("Created an expense " + expense.getExpenseId() + " involving " + expense.getSplitList().size() + " members having expense amount as " + expense.getExpenseAmount() + " paid by user " + expenseService.getUuidUserMap().get(expense.getPaidByUserId()).getName());
    }

    public void getExpense(ExpenseService expenseService, Expense expense) {
        System.out.println("The expense having id " + expense.getExpenseId() + " and name " + expense.getExpenseMetadata().getName() + " is paid by " + expenseService.getUuidUserMap().get(expense.getPaidByUserId()) + " and is of amount " + expense.getExpenseAmount());
    }

    public void getAllExpenses(ExpenseService expenseService) {
        System.out.println("All the expenses are listed below -");
        for (Map.Entry<UUID, Expense> expenseMap : expenseService.getExpenseMap().entrySet()) {
            System.out.println(expenseMap.getKey() + " " + expenseMap.getValue().getExpenseAmount() + " " + expenseService.getUuidUserMap().get(expenseMap.getValue().getPaidByUserId()).getName());
        }
    }

    public void createGroup(Group group) {
        System.out.println("Created group named " + group.getName() + " having group id " + group.getGroupId());
    }

    public void addUserToGroup(User user, Group group) {
        System.out.println("Added user " + user.getName() + " to group " + group.getName());
    }

    public void getGroupExpenses(Group group) {
        System.out.println("The expenses for the group " + group.getName() + " are -");
    }

    public void getExpensesByUserId(User user) {
        System.out.println("The expenses for the user " + user.getName() + " are -");
    }

    public void getExpensesOfUserIdByGroup(User user, Group group) {
        System.out.println("The expenses for the user " + user.getName() + " in the group " + group.getName() + " are -");
    }

    public void settleGroup(Group group) {
        System.out.println("The transactions to settle the group " + group.getName() + " are -");
    }
}
