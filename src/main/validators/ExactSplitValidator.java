package main.validators;

import main.exceptions.InvalidSplitDistributionException;
import main.exceptions.InvalidSplitTypeException;
import main.models.Split;
import main.models.SplitType;

import java.util.List;

public class ExactSplitValidator implements ISplitValidator{
    private List<Split> splitList;

    public ExactSplitValidator(List<Split> splitList) {
        this.splitList = splitList;
    }

    @Override
    public boolean areSplitsValid(double expenseAmount, List<Split> splitList) {
        double totalAmount = 0;
        for (int i = 0; i < splitList.size(); i++) {
            if (splitList.get(i).getSplitType() != SplitType.EXACT) {
                throw new InvalidSplitTypeException();
            }
            totalAmount += splitList.get(i).getShareAmount();
        }


        return totalAmount == expenseAmount;
    }
}
