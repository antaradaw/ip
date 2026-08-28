package bambolino.parser;

/**
 * Separates a full user input into a command and its arguments.
 */
public class Parser {
    /**
     * Parses user input into a normalized command and trimmed arguments.
     *
     * @param userInput The full command entered by the user.
     * @return The parsed command.
     */
    public Command parse(String userInput) {
        String[] commandParts = userInput.split("\\s+", 2);
        String command = commandParts.length == 0 ? "" : commandParts[0].toLowerCase();
        String arguments = commandParts.length == 2 ? commandParts[1].trim() : "";
        return new Command(command, arguments);
    }

    /** Stores the command name and its arguments. */
    public record Command(String name, String arguments) {
    }
}
