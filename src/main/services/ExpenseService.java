package main.services;

import main.exceptions.GroupNotFoundException;
import main.exceptions.InvalidSplitException;
import main.exceptions.UserNotFoundException;
import main.models.*;
import main.validators.ISplitValidator;

import java.util.*;

public class ExpenseService {
    private Map<UUID, Expense> expenseMap;
    private Map<UUID, User> uuidUserMap;
    private Map<String, Map<UUID, Map<UUID, Double>>> groupedUserToUserBalances;
    private Map<UUID, Group> uuidGroupMap;

    public ExpenseService() {
        this.expenseMap = new HashMap<>();
        this.uuidUserMap = new HashMap<>();
        this.groupedUserToUserBalances = new HashMap<>();
        this.uuidGroupMap = new HashMap<>();
        this.groupedUserToUserBalances.put("non_group", new HashMap<>());
    }

    // This method creates the expense instance
    private Expense createExpense(ISplitValidator splitValidator, UUID paidByUserId, double expenseAmount, List<Split> splitList, SplitType splitType, ExpenseMetadata expenseMetadata, String groupId) {
        if (!splitValidator.areSplitsValid(expenseAmount, splitList)) {
            throw new InvalidSplitException();
        }
        switch (splitType) {
            case EQUAL:
                for (Split split : splitList) {
                    double roundedOffShareAmount = ((double) Math.round(expenseAmount * 100 / splitList.size())) / 100.0;
                    split.setSplitType(SplitType.EQUAL);
                    split.setShareAmount(roundedOffShareAmount);
                }
                double roundedOffShareAmount = splitList.getFirst().getShareAmount();
                splitList.getFirst().setShareAmount(roundedOffShareAmount + expenseAmount - roundedOffShareAmount * splitList.size());
                break;
            case EXACT:
                for (Split split : splitList) {
                    split.setSplitType(SplitType.EXACT);
                }
                break;
            case PERCENT:
                for (Split split : splitList) {
                    double percentShareAmount = (split.getShareAmount() * expenseAmount) / 100.0;
                    split.setSplitType(SplitType.PERCENT);
                    split.setShareAmount(percentShareAmount);
                }
                break;
            default:
                return null;
        }
        Expense expense = new Expense(paidByUserId, expenseAmount, splitList, expenseMetadata, groupId);
        this.expenseMap.put(expense.getExpenseId(), expense);
        return expense;
    }

    // This method calls the creation of the expense object and puts it in the respective maps
    public Expense addExpense(ISplitValidator splitValidator, UUID paidByUserId, double expenseAmount, List<Split> splitList, SplitType splitType, ExpenseMetadata expenseMetadata, String groupId) {
        Expense expense = this.createExpense(splitValidator, paidByUserId, expenseAmount, splitList, splitType, expenseMetadata, groupId);
        Map<UUID, Map<UUID, Double>> userToUserBalances = this.groupedUserToUserBalances.get("non_group");
        if (!groupId.equals("null")) {
            userToUserBalances = this.groupedUserToUserBalances.get(groupId);
        }
        for (Split split : expense.getSplitList()) {
            UUID paidToUserId = split.getSplitUser();

            Map<UUID, Double> paidToUsersBalances = userToUserBalances.get(paidByUserId);
            if (!paidToUsersBalances.containsKey(paidToUserId)) {
                paidToUsersBalances.put(paidToUserId, 0.0);
            }
            double paidToUserCurrBalance = paidToUsersBalances.get(paidToUserId);
            paidToUsersBalances.put(paidToUserId, paidToUserCurrBalance + split.getShareAmount());
            userToUserBalances.put(paidByUserId, paidToUsersBalances);
            this.groupedUserToUserBalances.put(groupId, userToUserBalances);


            Map<UUID, Double> paidByUsersBalances = userToUserBalances.get(paidToUserId);
            if (!paidByUsersBalances.containsKey(paidByUserId)) {
                paidByUsersBalances.put(paidByUserId, 0.0);
            }
            double paidByUserCurrBalance = paidByUsersBalances.get(paidByUserId);
            paidByUsersBalances.put(paidByUserId, paidByUserCurrBalance - split.getShareAmount());
            userToUserBalances.put(paidToUserId, paidByUsersBalances);
            this.groupedUserToUserBalances.put(groupId, userToUserBalances);
        }
        this.expenseMap.put(expense.getExpenseId(), expense);

        if (!groupId.equals("null")) {
            Group group = this.getUuidGroupMap().get(UUID.fromString(groupId));
            List<UUID> expenseList = group.getExpenseList();
            expenseList.add(expense.getExpenseId());
            group.setExpenseList(expenseList);
            this.uuidGroupMap.put(UUID.fromString(groupId), group);
        }
        return expense;
    }

    public void addUsersToHashMap(User user) {
        this.uuidUserMap.put(user.getUserId(), user);
        this.groupedUserToUserBalances.get("non_group").put(user.getUserId(), new HashMap<>());
    }

    public void getNetBalancesByGroup(UUID groupId) {
        if (!this.getUuidGroupMap().containsKey(groupId)) {
            throw new GroupNotFoundException();
        }
        for (Map.Entry<UUID, Map<UUID, Double>> userToUserGroupBalanceEntry : this.groupedUserToUserBalances.get(groupId.toString()).entrySet()) {
            double balance = 0;
            User user = this.uuidUserMap.get(userToUserGroupBalanceEntry.getKey());
            for (Map.Entry<UUID, Double> userGroupBalanceEntry : userToUserGroupBalanceEntry.getValue().entrySet()) {
                balance -= userGroupBalanceEntry.getValue();
            }
            this.printUserBalances(user, balance);
        }
    }

    public void getNetBalancesByUserId(UUID userId) {
        if (!this.uuidUserMap.containsKey(userId)) {
            throw new UserNotFoundException();
        }
        double balance = 0;
        for (Map.Entry<UUID, Double> userNonGroupBalanceEntry : this.groupedUserToUserBalances.get("non_group").get(userId).entrySet()) {
            balance -= userNonGroupBalanceEntry.getValue();
        }
        User user = this.uuidUserMap.get(userId);
        List<UUID> groupIdList = user.getGroupIdList();
        for (UUID groupId : groupIdList) {
            for (Map.Entry<UUID, Double> userGroupBalanceEntry : this.groupedUserToUserBalances.get(groupId.toString()).get(userId).entrySet()) {
                balance -= userGroupBalanceEntry.getValue();
            }
        }
        this.printUserBalances(user, balance);
    }

    public void getNetBalancesOfUserIdByGroup(UUID userId, UUID groupId) {
        if (!this.uuidUserMap.containsKey(userId)) {
            throw new UserNotFoundException();
        }
        if (!this.getGroupedUserToUserBalances().containsKey(groupId.toString())) {
            throw new GroupNotFoundException();
        }
        double balance = 0;
        User user = this.uuidUserMap.get(userId);
        for (Map.Entry<UUID, Double> userGroupBalanceEntry : this.groupedUserToUserBalances.get(groupId.toString()).get(userId).entrySet()) {
            balance -= userGroupBalanceEntry.getValue();
        }
        this.printUserBalances(user, balance);
    }

    public void settleGroup(UUID groupId) {
        if (!this.groupedUserToUserBalances.containsKey(groupId.toString())) {
            throw new GroupNotFoundException();
        }
        Comparator<BalanceUserPair> ascending = Comparator.comparingDouble(BalanceUserPair::getBalance);
        PriorityQueue<BalanceUserPair> positiveBalancePriorityQueue = new PriorityQueue<>(ascending.reversed());
        PriorityQueue<BalanceUserPair> negativeBalancePriorityQueue = new PriorityQueue<>(ascending);
        for (Map.Entry<UUID, Map<UUID, Double>> userToUserGroupBalanceEntry : this.groupedUserToUserBalances.get(groupId.toString()).entrySet()) {
            double balance = 0;
            User user = this.uuidUserMap.get(userToUserGroupBalanceEntry.getKey());
            for (Map.Entry<UUID, Double> userGroupBalanceEntry : userToUserGroupBalanceEntry.getValue().entrySet()) {
                balance -= userGroupBalanceEntry.getValue();
            }
            if (balance > 0) {
                BalanceUserPair balanceUserPair = new BalanceUserPair(user.getUserId(), balance);
                positiveBalancePriorityQueue.add(balanceUserPair);
            }
            if (balance < 0) {
                BalanceUserPair balanceUserPair = new BalanceUserPair(user.getUserId(), balance);
                negativeBalancePriorityQueue.add(balanceUserPair);
            }
        }
        this.printTransactions(positiveBalancePriorityQueue, negativeBalancePriorityQueue);
    }

//    public void getAllBalances() {
//        for (Map.Entry<UUID, Map<UUID, Double>> paidByUsersCurrBalance : this.userToUserBalances.entrySet()) {
//            for (Map.Entry<UUID, Double> paidToUsersCurrBalance : paidByUsersCurrBalance.getValue().entrySet()) {
//                double amountToBePaid = paidToUsersCurrBalance.getValue();
//                if (amountToBePaid > 0) {
//                    this.printBalance(this.uuidUserMap.get(paidByUsersCurrBalance.getKey()), this.uuidUserMap.get(paidToUsersCurrBalance.getKey()), amountToBePaid);
//                }
//            }
//        }
//    }

//    private void printBalance(User user1, User user2, double amountToBePaid) {
//        if (amountToBePaid > 0) {
//            System.out.println(user2.getName() + " needs to pay " + amountToBePaid + " to " + user1.getName());
//        } else {
//            System.out.println(user1.getName() + " needs to pay " + Math.abs(amountToBePaid) + " to " + user2.getName());
//        }
//    }

    private void printUserBalances(User user, Double balance) {
        if (balance < 0) {
            System.out.println("User " + user.getName() + " needs to be paid " + Math.abs(balance) + " amount");
        } else if (balance > 0) {
            System.out.println("User " + user.getName() + " has to pay " + balance + " amount");
        } else {
            System.out.println("User " + user.getName() + " is all settled");
        }
    }

    private void printTransactions(PriorityQueue<BalanceUserPair> pqPositive, PriorityQueue<BalanceUserPair> pqNegative) {
        while(!pqPositive.isEmpty() && !pqNegative.isEmpty()) {
            BalanceUserPair balanceUserPairPositive = pqPositive.poll();
            BalanceUserPair balanceUserPairNegative = pqNegative.poll();
            double resultantBalance = balanceUserPairPositive.getBalance() + balanceUserPairNegative.getBalance();
            if (resultantBalance > 0) {
                balanceUserPairPositive.setBalance(resultantBalance);
                pqPositive.add(balanceUserPairPositive);
            } else if (resultantBalance < 0) {
                balanceUserPairNegative.setBalance(resultantBalance);
                pqNegative.add(balanceUserPairNegative);
            }
            if (resultantBalance >= 0) {
                System.out.println("User " + this.uuidUserMap.get(balanceUserPairPositive.getUserId()).getName() + " pays " + Math.abs(balanceUserPairNegative.getBalance()) + " to User " + this.uuidUserMap.get(balanceUserPairNegative.getUserId()).getName());
            } else {
                System.out.println("User " + this.uuidUserMap.get(balanceUserPairPositive.getUserId()).getName() + " pays " + balanceUserPairPositive.getBalance() + " to User " + this.uuidUserMap.get(balanceUserPairNegative.getUserId()).getName());
            }
        }
    }

    public Map<UUID, User> getUuidUserMap() {
        return uuidUserMap;
    }

    public void setUuidUserMap(Map<UUID, User> uuidUserMap) {
        this.uuidUserMap = uuidUserMap;
    }

    public Map<UUID, Expense> getExpenseMap() {
        return expenseMap;
    }

    public void setExpenseMap(Map<UUID, Expense> expenseMap) {
        this.expenseMap = expenseMap;
    }

    public Map<String, Map<UUID, Map<UUID, Double>>> getGroupedUserToUserBalances() {
        return groupedUserToUserBalances;
    }

    public void setGroupedUserToUserBalances(Map<String, Map<UUID, Map<UUID, Double>>> groupedUserToUserBalances) {
        this.groupedUserToUserBalances = groupedUserToUserBalances;
    }

    public Map<UUID, Group> getUuidGroupMap() {
        return uuidGroupMap;
    }

    public void setUuidGroupMap(Map<UUID, Group> uuidGroupMap) {
        this.uuidGroupMap = uuidGroupMap;
    }
}
