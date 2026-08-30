package tardt.task;

/**
 * An enum class that handles the conversion from a numeric string to its shorthand month String representation.
 */
public enum Month {
    JAN("01", "Jan"),
    FEB("02", "Feb"),
    MAR("03", "Mar"),
    APR("04", "Apr"),
    MAY("05", "May"),
    JUN("06", "Jun"),
    JUL("07", "Jul"),
    AUG("08", "Aug"),
    SEP("09", "Sep"),
    OCT("10", "Oct"),
    NOV("11", "Nov"),
    DEC("12", "Dec");

    private final String number;
    private final String shortName;

    private Month(String number, String shortName) {
        this.number = number;
        this.shortName = shortName;
    }

    public String getNumber() {
        return number;
    }

    public String getShortName() {
        return shortName;
    }

    /**
     * Outputs the short name of a month corresponding to the input number.
     *
     * @param number A String from '01' to '12' corresponding to the months of the year.
     * @return The corresponding short name for that month.
     */
    public static String getShortNameByNumber(String number) {
        for (Month m : values()) {
            if (m.number.equals(number)) {
                return m.shortName;
            }
        }
        return null;
    }
}
