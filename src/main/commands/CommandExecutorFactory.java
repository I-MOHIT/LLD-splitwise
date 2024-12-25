package main.commands;

import main.OutputPrinter;
import main.exceptions.InvalidCommandException;
import main.models.Command;
import main.services.ExpenseService;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutorFactory {
    private ExpenseService expenseService;
    private OutputPrinter outputPrinter;
    private Map<String, CommandExecutor> commandExecutorMap;

    public CommandExecutorFactory(ExpenseService expenseService, OutputPrinter outputPrinter) {
        this.expenseService = expenseService;
        this.outputPrinter = outputPrinter;
        this.commandExecutorMap = new HashMap<>();
        this.commandExecutorMap.put(ExitCommandExecutor.COMMAND_NAME, new ExitCommandExecutor(outputPrinter, expenseService));
        this.commandExecutorMap.put(CreateUserCommandExecutor.COMMAND_NAME, new CreateUserCommandExecutor(outputPrinter, expenseService));
        this.commandExecutorMap.put(GetNetBalancesByUserIdCommandExecutor.COMMAND_NAME, new GetNetBalancesByUserIdCommandExecutor(outputPrinter, expenseService));
        this.commandExecutorMap.put(GetGroupBalancesCommandExecutor.COMMAND_NAME, new GetGroupBalancesCommandExecutor(outputPrinter, expenseService));
        this.commandExecutorMap.put(CreateExpenseCommandExecutor.COMMAND_NAME, new CreateExpenseCommandExecutor(outputPrinter, expenseService));
        this.commandExecutorMap.put(GetExpenseByIdCommandExecutor.COMMAND_NAME, new GetExpenseByIdCommandExecutor(outputPrinter, expenseService));
        this.commandExecutorMap.put(GetAllExpensesCommandExecutor.COMMAND_NAME, new GetAllExpensesCommandExecutor(outputPrinter, expenseService));
        this.commandExecutorMap.put(AddUserToGroupCommandExecutor.COMMAND_NAME, new AddUserToGroupCommandExecutor(outputPrinter,expenseService));
        this.commandExecutorMap.put(CreateGroupCommandExecutor.COMMAND_NAME, new CreateGroupCommandExecutor(outputPrinter, expenseService));
        this.commandExecutorMap.put(GetGroupExpensesCommandExecutor.COMMAND_NAME, new GetGroupExpensesCommandExecutor(outputPrinter, expenseService));
        this.commandExecutorMap.put(GetNetBalancesOfUserIdByGroupCommandExecutor.COMMAND_NAME, new GetNetBalancesOfUserIdByGroupCommandExecutor(outputPrinter, expenseService));
        this.commandExecutorMap.put(GetExpensesByUserIdCommandExecutor.COMMAND_NAME, new GetExpensesByUserIdCommandExecutor(outputPrinter, expenseService));
        this.commandExecutorMap.put(GetExpensesOfUserIdByGroupCommandExecutor.COMMAND_NAME, new GetExpensesOfUserIdByGroupCommandExecutor(outputPrinter, expenseService));
        this.commandExecutorMap.put(SettleGroupCommandExecutor.COMMAND_NAME, new SettleGroupCommandExecutor(outputPrinter, expenseService));
    }

    public CommandExecutor getCommandExecutor(Command command) {
        String commandName = command.getCommandName();
        if (!commandExecutorMap.containsKey(commandName)) {
            throw new InvalidCommandException();
        }
        return commandExecutorMap.get(commandName);
    }

    public ExpenseService getExpenseService() {
        return expenseService;
    }

    public void setExpenseService(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    public OutputPrinter getOutputPrinter() {
        return outputPrinter;
    }

    public void setOutputPrinter(OutputPrinter outputPrinter) {
        this.outputPrinter = outputPrinter;
    }

    public Map<String, CommandExecutor> getCommandExecutorMap() {
        return commandExecutorMap;
    }

    public void setCommandExecutorMap(Map<String, CommandExecutor> commandExecutorMap) {
        this.commandExecutorMap = commandExecutorMap;
    }
}
