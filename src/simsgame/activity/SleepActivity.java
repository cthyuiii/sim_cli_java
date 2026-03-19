package simsgame.activity;

import simsgame.engine.GameContext;
import simsgame.sims.SimCharacter;

/**
 * Sleep Activity
 * Recover energy needs.
 */
public class SleepActivity extends AbstractActivity {

    private final int energyGain;

    /**
     * Constructor with specified energy gain and duration.
     * @param energyGain Energy value restored for sleep.
     * @param hours Duration spent eating.
     */
    public SleepActivity(int energyGain, int hours) {
        super("Sleep", hours);
        this.energyGain = energyGain;
    }

    /**
     * Executes sleeping action.
     * Increases the Sim's energy level by the defined gain
     * Decreases their hunger level by a small fixed amount for realism.
     * @param sim Sim that is sleeping.
     * @param context Printing notifications and advancing time.
     */
    @Override
    public void apply(SimCharacter sim, GameContext context) {
        sim.getNeeds().addEnergy(energyGain);

        sim.getNeeds().addHunger(-10);

        context.notify(sim.getName() + " slept (+" + energyGain + " energy).");
    }
}