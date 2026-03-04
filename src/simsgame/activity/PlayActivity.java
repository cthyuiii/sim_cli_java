package simsgame.activity;

import simsgame.engine.GameContext;
import simsgame.sims.SimCharacter;

public class PlayActivity extends AbstractActivity {

    private final int funGain;
    private final int energyCost;
    private final int cost;

    public PlayActivity(int funGain, int energyCost, int hours, int cost) {
        super("Play", hours);
        this.funGain = funGain;
        this.energyCost = energyCost;
        this.cost = cost;
    }

    @Override
    public void apply(SimCharacter sim, GameContext context) {
        boolean paid = (cost <= 0) || sim.getWallet().spend(cost);

        if (!paid) {
            context.notify(sim.getName() + " couldn't afford to play right now.");
            return;
        }

        sim.getNeeds().addFun(funGain);
        sim.getNeeds().addEnergy(-energyCost);

        if (cost > 0) {
            context.notify(sim.getName() + " played (-$" + cost + ", +" + funGain + " fun, -" + energyCost + " energy).");
        } else {
            context.notify(sim.getName() + " played (+" + funGain + " fun, -" + energyCost + " energy).");
        }
    }
}