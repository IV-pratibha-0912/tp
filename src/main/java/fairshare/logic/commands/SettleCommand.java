package fairshare.logic.commands;

import fairshare.model.Model;
import fairshare.model.expense.Expense;

public class SettleCommand extends Command {
    private static final String MESSAGE_SUCCESS = "Settlement added successfully";
    private Expense settlement;

    public SettleCommand(Expense settlement) {
        this.settlement = settlement;
    }

    public CommandResult execute(Model model) {
        model.addExpense(settlement);
        return new CommandResult(MESSAGE_SUCCESS, false, false);
    }

}
