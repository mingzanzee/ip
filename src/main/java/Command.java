/** Represents the supported command keywords and an unrecognised input. */
public enum Command {
    BYE("bye"),
    LIST("list"),
    MARK("mark"),
    UNMARK("unmark"),
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    DELETE("delete"),
    INVALID(null);

    private final String keyword;

    Command(String keyword) {
        this.keyword = keyword;
    }

    /** Returns the command that matches the input keyword, if one exists. */
    static Command fromKeyword(String keyword) {
        for (Command command : values()) {
            if (keyword.equals(command.keyword)) {
                return command;
            }
        }
        return INVALID;
    }
}