package simsgame.activity;

import simsgame.engine.GameContext;
import simsgame.sims.SimCharacter;

/**
 * Shower Activity restores the Sim's hygiene levels without cost.
 */
public class ShowerActivity extends AbstractActivity {

    private final int hygieneGain;

    /**
     * Constructor specified hygiene gain and duration.
     * @param hygieneGain Hygiene value restored for shower..
     * @param hours Duration spent showering.
     */
    public ShowerActivity(int hygieneGain, int hours) {
        super("Shower", hours);
        this.hygieneGain = hygieneGain;
    }

    // =====================================================
    // Core behaviour
    // =====================================================
    /**
     * Executes shower action.
     * Increases the Sim's hygiene level by the predefined amount and 
     * sends a confirmation notification to the game engine.
     * @param sim Sim that is showering.
     * @param context Printing notifications and advancing time.
     */
    @Override
    public void apply(SimCharacter sim, GameContext context) {
        sim.getNeeds().addHygiene(hygieneGain);
        context.notify(sim.getName() + " took a shower (+" + hygieneGain + " hygiene).");
    }
}