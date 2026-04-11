package fairshare.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import fairshare.model.Model;

public class ExitCommandTest {

    // Test exit command displays correct user message and sets the appropriate flag.
    @Test
    public void execute_exit_success() {
        Model model = Mockito.mock(Model.class);
        ExitCommand exitCmd = new ExitCommand();

        CommandResult cmdRes = exitCmd.execute(model);

        assertFalse(cmdRes.getIsHelp());
        assertTrue(cmdRes.getIsExit());

        String successMessage = "Shutting down FairShare...\nSee you soon!";
        assertEquals(successMessage, cmdRes.getResponse());
    }

}
