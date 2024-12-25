package main.models;

import java.util.UUID;

public class BalanceUserPair {
    private UUID userId;
    private double balance;

    public BalanceUserPair(UUID userId, double balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
