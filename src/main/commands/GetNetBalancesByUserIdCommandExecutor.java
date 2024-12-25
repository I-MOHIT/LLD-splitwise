package main.commands;

import main.OutputPrinter;
import main.exceptions.InvalidCommandException;
import main.models.Command;
import main.services.ExpenseService;

import java.util.UUID;

public class GetNetBalancesByUserIdCommandExecutor extends CommandExecutor{
    public static String COMMAND_NAME = "get_net_balances_by_user_id";

    public GetNetBalancesByUserIdCommandExecutor(OutputPrinter outputPrinter, ExpenseService expenseService) {
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
        UUID userId = UUID.fromString(command.getParams().getFirst());
        expenseService.getNetBalancesByUserId(userId);
    }
}
