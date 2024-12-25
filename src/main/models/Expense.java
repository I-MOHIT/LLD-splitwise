package main.models;

import java.util.List;
import java.util.UUID;

public class Expense {
    private UUID expenseId;
    private UUID paidByUserId;
    private double expenseAmount;
    private List<Split> splitList;
    private ExpenseMetadata expenseMetadata;
    private String groupId;

    public Expense() {
    }

    public Expense(UUID paidByUserId, double expenseAmount, List<Split> splitList, ExpenseMetadata expenseMetadata, String groupId) {
        this.expenseId = UUID.randomUUID();
        this.paidByUserId = paidByUserId;
        this.expenseAmount = expenseAmount;
        this.splitList = splitList;
        this.expenseMetadata = expenseMetadata;
        this.groupId = groupId;
    }

    public UUID getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(UUID expenseId) {
        this.expenseId = expenseId;
    }

    public UUID getPaidByUserId() {
        return paidByUserId;
    }

    public void setPaidByUserId(UUID paidByUserId) {
        this.paidByUserId = paidByUserId;
    }

    public double getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(double expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public List<Split> getSplitList() {
        return splitList;
    }

    public void setSplitList(List<Split> splitList) {
        this.splitList = splitList;
    }

    public ExpenseMetadata getExpenseMetadata() {
        return expenseMetadata;
    }

    public void setExpenseMetadata(ExpenseMetadata expenseMetadata) {
        this.expenseMetadata = expenseMetadata;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
