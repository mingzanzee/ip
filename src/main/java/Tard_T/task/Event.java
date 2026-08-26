package Tard_T.task;

import Tard_T.exception.TardTException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * A type of Task that has a start and end time associated with it.
 */
public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    public Event(String description, String from, String to) throws TardTException {
        super(description);

        try {
            this.from = LocalDateTime.parse(from);
            this.to = LocalDateTime.parse(to);
        } catch (DateTimeParseException e) {
            // Try alternative formats or throw a custom exception
            throw new TardTException("Invalid date/time format. Use yyyy-MM-ddTHH:mm:ss");
        }
    }

    /**
     * Outputs the start date and time in a more readable format.
     * For example: Oct 12 2026 12:00
     *
     * @return A String in the following form: {shortname of month} {day} {year} {time}.
     */
    public String getFrom() {
        String unparsed = this.from.toString();
        String ymd = unparsed.split("T")[0];
        String time = unparsed.split("T")[1];
        String[] ymd_split = ymd.split("-");
        String year = ymd_split[0];
        String month = ymd_split[1];
        String day = ymd_split[2];

        return Month.getShortNameByNumber(month) + " " + day + " " + year + " " + time;
    }

    /**
     * Outputs the end date and time in a more readable format.
     * For example: Oct 12 2026 12:00
     *
     * @return A String in the following form: {shortname of month} {day} {year} {time}.
     */
    public String getTo() {
        String unparsed = this.to.toString();
        String ymd = unparsed.split("T")[0];
        String time = unparsed.split("T")[1];
        String[] ymd_split = ymd.split("-");
        String year = ymd_split[0];
        String month = ymd_split[1];
        String day = ymd_split[2];

        return Month.getShortNameByNumber(month) + " " + day + " " + year + " " + time;
    }

    /**
     * Outputs the start date and time in ISO-8601 format
     *
     * @return A String representing date-time in ISO-8601 format.
     */
    public String getFromRaw() {
        return this.from.toString();
    }

    /**
     * Outputs the end date and time in ISO-8601 format
     *
     * @return A String representing date-time in ISO-8601 format.
     */
    public String getToRaw() {
        return this.to.toString();
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.getFrom() + " to: " + this.getTo() + ")";
    }
}