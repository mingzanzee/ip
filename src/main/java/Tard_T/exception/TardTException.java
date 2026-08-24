package Tard_T.exception;

/**
 * Represents a user-facing error caused by an invalid Tard_T.Tard_T command or command format.
 */
public class TardTException extends Exception {
    /** Creates an exception with the message that should be shown to the user. */
    public TardTException(String message) {
        super(message);
    }
}
