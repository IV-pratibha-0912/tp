package fairshare.logic.commands;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandResultTest {

    // Test creating an instance of CommandResult with a null response.
    @Test
    public void constructor_nullResponse_throwsAssertionError() {
        String response = null;
        assertThrows(AssertionError.class, () -> new CommandResult(response, false, false ));
    }

    // Test creating an instance of CommandResult of a command that is not help or exit.
    @Test
    public void constructor_normalCommand_success() {
        String response = "not help, not exit";
        CommandResult cmdRes = assertDoesNotThrow(() -> new CommandResult(response, false, false));

        assertEquals(response, cmdRes.getResponse());
        assertFalse(cmdRes.getIsHelp());
        assertFalse(cmdRes.getIsExit());
    }

    // Test creating an instance of CommandResult of a HelpCommand.
    @Test
    public void constructor_helpCommand_success() {
        String response = "help";
        CommandResult cmdRes = assertDoesNotThrow(() -> new CommandResult(response, true, false));

        assertEquals(response, cmdRes.getResponse());
        assertTrue(cmdRes.getIsHelp());
        assertFalse(cmdRes.getIsExit());
    }

    // Test creating an instance of CommandResult of an ExitCommand.
    @Test
    public void constructor_exitCommand_success() {
        String response = "exit";
        CommandResult cmdRes = assertDoesNotThrow(() -> new CommandResult(response, false, true));

        assertEquals(response, cmdRes.getResponse());
        assertFalse(cmdRes.getIsHelp());
        assertTrue(cmdRes.getIsExit());
    }
}
