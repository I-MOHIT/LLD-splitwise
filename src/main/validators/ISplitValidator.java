package main.validators;

import main.models.Split;

import java.util.List;

public interface ISplitValidator {
    public abstract boolean areSplitsValid(double expenseAmount, List<Split> splitList);
}
