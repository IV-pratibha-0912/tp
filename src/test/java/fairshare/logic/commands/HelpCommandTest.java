package fairshare.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import fairshare.model.Model;

public class HelpCommandTest {

    // Test help command displays correct user message and sets the appropriate flag.
    @Test
    public void execute_help_success() {
        Model model = Mockito.mock(Model.class);
        HelpCommand helpCmd = new HelpCommand();

        CommandResult cmdRes = helpCmd.execute(model);

        assertTrue(cmdRes.getIsHelp());
        assertFalse(cmdRes.getIsExit());

        String successMessage = "Displaying help window";
        assertEquals(successMessage, cmdRes.getResponse());
    }
}
