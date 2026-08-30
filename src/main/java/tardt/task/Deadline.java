package tardt.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import tardt.exception.TardTException;

/**
 * A type of Task that has a deadline associated with it.
 */
public class Deadline extends Task {
    protected LocalDateTime by;

    /**
     * Constructor for Deadline task.
     * @param description Description for Deadline task.
     * @param by Deadline of the Deadline task.
     * @throws TardTException Unique exception of the TardT class
     */
    public Deadline(String description, String by) throws TardTException {
        super(description);
        try {
            this.by = LocalDateTime.parse(by);
        } catch (DateTimeParseException e) {
            // Try alternative formats or throw a custom exception
            throw new TardTException("Invalid date/time format. Use yyyy-MM-ddTHH:mm:ss");
        }
    }

    /**
     * Returns the deadline as a string in a different format as the expected input.
     *
     * @return A string represent datetime in MMM dd yyyy time.
     */
    public String getBy() {
        String unparsed = this.by.toString();
        String ymd = unparsed.split("T")[0];
        String time = unparsed.split("T")[1];
        String[] ymdSplits = ymd.split("-");
        String year = ymdSplits[0];
        String month = ymdSplits[1];
        String day = ymdSplits[2];

        return Month.getShortNameByNumber(month) + " " + day + " " + year + " " + time;
    }

    /**
     * Outputs the date-time of Task deadline as a String.
     * Leaves it in one of the ISO-8601 formats.
     *
     * @return The date and time of the deadline of the Task as a String unchanged.
     */
    public String getByRaw() {
        return this.by.toString();
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.getBy() + ")";
    }
}
