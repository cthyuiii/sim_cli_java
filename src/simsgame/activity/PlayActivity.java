package simsgame.activity;

import simsgame.engine.GameContext;
import simsgame.sims.SimCharacter;

/**
 * Play activity to increase Fun need.
 * Reduces Energy and Money
 */
public class PlayActivity extends AbstractActivity {
    private final int funGain;
    private final int energyCost;
    private final int cost;

    /**
     * Constructor with specific gains and costs.
     * @param funGain Fun to add to Sim's needs.
     * @param energyCost Energy to subtract from the Sim's needs.
     * @param hours Duration of the activity in hours.
     * @param cost Cost of the activity.
     */
    public PlayActivity(int funGain, int energyCost, int hours, int cost) {
        super("Play", hours);
        this.funGain = funGain;
        this.energyCost = energyCost;
        this.cost = cost;
    }

    /**
     * Executes play action.
     * If affordable, update Sim's fun and energy levels and sends 
     * a notification. If the Sim cannot afford it, the activity is cancelled.
     * @param sim Sim that is playing.
     * @param context Printing notifications and advancing time.
     */
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