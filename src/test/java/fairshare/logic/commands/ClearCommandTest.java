package fairshare.logic.commands;

import fairshare.model.Model;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

public class ClearCommandTest {

    // Test clearing the expense list with a null model.
    @Test
    public void execute_nullModel_throwsAssertionError() {
        Model model = null;

        ClearCommand clearCmd = new ClearCommand();
        assertThrows(AssertionError.class, () -> clearCmd.execute(model));
    }

    // Test clearing the expense list.
    @Test
    public void execute_clear_success() {
        Model model = Mockito.mock(Model.class);

        ClearCommand clearCmd = new ClearCommand();
        CommandResult cmdRes = clearCmd.execute(model);

        String successMessage = "Expense list cleared successfully.";
        assertEquals(successMessage, cmdRes.getResponse());

        verify(model).clearExpenseList(); // Verify clearExpenseList was called.
    }
}
