package simsgame.activity;

import simsgame.engine.GameContext;
import simsgame.sims.SimCharacter;

/**
 * Activity Interface
 */
public interface Activity {

    String name();
    int hours();

    /**
     * Applies effects of activity to the Sim.
     * @param sim The SimCharacter performing the activity.
     * @param context The GameContext used to notify the system or advance time.
     */
    void apply(SimCharacter sim, GameContext context);
}
