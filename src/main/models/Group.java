package main.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Group {
    private UUID groupId;
    private String name;
    private List<UUID> expenseList;
    private List<UUID> userList;

    public Group(String name) {
        this.groupId = UUID.randomUUID();
        this.name = name;
        this.expenseList = new ArrayList<>();
        this.userList = new ArrayList<>();
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UUID> getExpenseList() {
        return expenseList;
    }

    public void setExpenseList(List<UUID> expenseList) {
        this.expenseList = expenseList;
    }

    public List<UUID> getUserList() {
        return userList;
    }

    public void setUserList(List<UUID> userList) {
        this.userList = userList;
    }
}
