package Tard_T.task;

import Tard_T.exception.TardTException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * A type of Task that has a deadline associated with it.
 */
public class Deadline extends Task {
    protected LocalDateTime by;

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
     * Returns the deadline as a string in a different format as the expected input
     * @return A string represent datetime in MMM dd yyyy time
     */
    public String getBy() {
        String unparsed = this.by.toString();
        String ymd = unparsed.split("T")[0];
        String time = unparsed.split("T")[1];
        String[] ymd_split = ymd.split("-");
        String year = ymd_split[0];
        String month = ymd_split[1];
        String day = ymd_split[2];

        return Month.getShortNameByNumber(month) + " " + day + " " + year + " " + time;
    }

    public String getByRaw() {
        return this.by.toString();
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.getBy() + ")";
    }
}