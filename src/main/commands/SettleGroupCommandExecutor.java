package main.commands;

import main.OutputPrinter;
import main.exceptions.GroupNotFoundException;
import main.exceptions.InvalidCommandException;
import main.models.Command;
import main.models.Expense;
import main.models.Group;
import main.models.Split;
import main.services.ExpenseService;

import java.util.List;
import java.util.UUID;

public class SettleGroupCommandExecutor extends CommandExecutor{
    public static String COMMAND_NAME = "settle_group";

    public SettleGroupCommandExecutor(OutputPrinter outputPrinter, ExpenseService expenseService) {
        super(outputPrinter, expenseService);
    }

    @Override
    public boolean validateCommand(Command command) {
        if (!command.getCommandName().equals(COMMAND_NAME) || command.getParams().size() != 1) {
            throw new InvalidCommandException();
        }
        return true;
    }

    @Override
    public void executeCommand(Command command) {
        UUID groupId = UUID.fromString(command.getParams().getFirst());
        if (!this.expenseService.getUuidGroupMap().containsKey(groupId)) {
            throw new GroupNotFoundException();
        }
        Group group = this.expenseService.getUuidGroupMap().get(groupId);
        outputPrinter.settleGroup(group);
        this.expenseService.settleGroup(groupId);
    }
}
