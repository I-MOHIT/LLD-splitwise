package main.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private UUID userId;
    private String name;
    private List<UUID> groupIdList;
    private List<UUID> expenseList;

    public User(String name) {
        this.userId = UUID.randomUUID();
        this.name = name;
        this.groupIdList = new ArrayList<>();
        this.expenseList = new ArrayList<>();
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UUID> getGroupIdList() {
        return groupIdList;
    }

    public void setGroupIdList(List<UUID> groupIdList) {
        this.groupIdList = groupIdList;
    }

    public List<UUID> getExpenseList() {
        return expenseList;
    }

    public void setExpenseList(List<UUID> expenseList) {
        this.expenseList = expenseList;
    }
}
