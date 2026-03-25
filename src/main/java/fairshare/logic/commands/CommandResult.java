package fairshare.logic.commands;

public class CommandResult {
    private final String response;
    private final boolean isHelp;

    public CommandResult(String response, boolean isHelp) {
        this.response = response;
        this.isHelp = isHelp;
    }

    public String getResponse() {
        return this.response;
    }

    public boolean getIsHelp() {
        return this.isHelp;
    }
}
