package fairshare.logic.commands;

import fairshare.model.Model;
import fairshare.model.expense.Expense;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;

public class SettleCommandTest {

    // Test creating an instance of SettleCommand with a null settlement
    @Test
    public void constructor_nullSettlement_throwsAssertionError() {
        Expense settlement  = null;

        assertThrows(AssertionError.class, () -> new SettleCommand(settlement));
    }

    // Test executing settle command with a null model.
    @Test
    public void execute_nullModel_throwsAssertionError() {
        Model model = null;
        Expense settlement = Mockito.mock(Expense.class);
        SettleCommand settleCmd = new SettleCommand(settlement);

        assertThrows(AssertionError.class, () -> settleCmd.execute(model));
    }

    // Test executing settle command with a valid model and settlement.
    @Test
    public void execute_validModelAndSettlement_success() {
        Model model = Mockito.mock(Model.class);
        Expense settlement = Mockito.mock(Expense.class);
        SettleCommand settleCmd = new SettleCommand(settlement);
        CommandResult cmdRes = settleCmd.execute(model);

        String successMessage = "Settlement added successfully";
        assertEquals(successMessage, cmdRes.getResponse());

        assertFalse(cmdRes.getIsHelp());
        assertFalse(cmdRes.getIsExit());

        verify(model).addExpense(settlement); // Verify addExpense() was called with the correct Expense object.
    }
}
