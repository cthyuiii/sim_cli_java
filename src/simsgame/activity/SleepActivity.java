package simsgame.activity;

import simsgame.engine.GameContext;
import simsgame.sims.SimCharacter;

public class SleepActivity extends AbstractActivity {

    private final int energyGain;

    public SleepActivity(int energyGain, int hours) {
        super("Sleep", hours);
        this.energyGain = energyGain;
    }

    @Override
    public void apply(SimCharacter sim, GameContext context) {
        sim.getNeeds().addEnergy(energyGain);

        // Optional realism: sleeping makes you slightly hungry
        sim.getNeeds().addHunger(-10);

        context.notify(sim.getName() + " slept (+" + energyGain + " energy).");
    }
}