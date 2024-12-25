package main.validators;

import main.exceptions.InvalidSplitDistributionException;
import main.exceptions.InvalidSplitTypeException;
import main.models.Split;
import main.models.SplitType;

import java.util.List;

public class PercentSplitValidator implements ISplitValidator{
    private List<Split> splitList;

    public PercentSplitValidator(List<Split> splitList) {
        this.splitList = splitList;
    }

    @Override
    public boolean areSplitsValid(double expenseAmount, List<Split> splitList) {
        double totalPercent = 0;
        for (int i = 0; i < splitList.size(); i++) {
            if (splitList.get(i).getSplitType() != SplitType.PERCENT) {
                throw new InvalidSplitTypeException();
            }
            totalPercent += splitList.get(i).getShareAmount();
        }
        return totalPercent == 100.0;
    }
}
