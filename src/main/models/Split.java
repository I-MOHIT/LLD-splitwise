package main.models;

import java.util.UUID;

public class Split {
    private UUID splitUserId;
    private double shareAmount;
    private SplitType splitType;

    public Split(UUID splitUserId, double shareAmount, SplitType splitType) {
        this.splitUserId = splitUserId;
        this.shareAmount = shareAmount;
        this.splitType = splitType;
    }

    public UUID getSplitUser() {
        return splitUserId;
    }

    public void setSplitUserId(UUID splitUserId) {
        this.splitUserId = splitUserId;
    }

    public double getShareAmount() {
        return shareAmount;
    }

    public void setShareAmount(double shareAmount) {
        this.shareAmount = shareAmount;
    }

    public SplitType getSplitType() {
        return splitType;
    }

    public void setSplitType(SplitType splitType) {
        this.splitType = splitType;
    }
}
