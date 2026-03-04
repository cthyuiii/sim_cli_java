package simsgame.activity;

public abstract class AbstractActivity implements Activity {
    private String name;
    private int hours;

    public AbstractActivity(String name, int hours) {
        this.name = name;
        this.hours = hours;
    }

    @Override
    public String name() { return name; }

    @Override
    public int hours() { return hours; }
}
