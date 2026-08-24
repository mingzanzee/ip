import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

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

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.getFrom() + " to: " + this.getTo() + ")";
    }
}