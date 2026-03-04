package simsgame.activity;

import simsgame.engine.GameContext;
import simsgame.sims.SimCharacter;

/**
 * Shared base for most activities.
 * Stores a name and a default time cost.
 */
public abstract class AbstractActivity implements Activity {

    private final String name;
    private final int hours;

    protected AbstractActivity(String name, int hours) {
        this.name = name;
        this.hours = hours;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int hours() {
        return hours;
    }

    @Override
    public abstract void apply(SimCharacter sim, GameContext context);
}