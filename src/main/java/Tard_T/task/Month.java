package Tard_T.task;

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

    public String getNumber() { return number; }
    public String getShortName() { return shortName; }

    public static String getShortNameByNumber(String number) {
        for (Month m : values()) {
            if (m.number.equals(number)) {
                return m.shortName;
            }
        }
        return null;
    }
}