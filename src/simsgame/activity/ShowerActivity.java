package simsgame.activity;

import simsgame.engine.GameContext;
import simsgame.sims.SimCharacter;

public class ShowerActivity extends AbstractActivity {

    private final int hygieneGain;

    public ShowerActivity(int hygieneGain, int hours) {
        super("Shower", hours);
        this.hygieneGain = hygieneGain;
    }

    @Override
    public void apply(SimCharacter sim, GameContext context) {
        sim.getNeeds().addHygiene(hygieneGain);
        context.notify(sim.getName() + " took a shower (+" + hygieneGain + " hygiene).");
    }
}