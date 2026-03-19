package simsgame.activity;

/**
 * Stores name and duration of activity in hours
 */
public abstract class AbstractActivity implements Activity {

    private final String name;
    private final int hours;

    /**
     * Constructor for activities.
     * 
     * @param name Name to be displayed.
     * @param hours Hours taken for activity to complete.
     */
    protected AbstractActivity(String name, int hours) {
        this.name = name;
        this.hours = hours;
    }

    // =====================================================
    // Getters
    // =====================================================
    @Override
    public String name() { return name; }

    @Override
    public int hours() { return hours; }
}
