package main.validators;

import main.exceptions.InvalidSplitDistributionException;
import main.exceptions.InvalidSplitTypeException;
import main.models.Split;
import main.models.SplitType;

import java.util.List;

public class EqualSplitValidator implements ISplitValidator{
    private List<Split> splitList;

    public EqualSplitValidator(List<Split> splitList) {
        this.splitList = splitList;
    }

    @Override
    public boolean areSplitsValid(double expenseAmount, List<Split> splitList) {
        for (int i = 0; i < splitList.size(); i++) {
            if (splitList.get(i).getSplitType() != SplitType.EQUAL) {
                throw new InvalidSplitTypeException();
            }
        }
        return true;
    }
}
