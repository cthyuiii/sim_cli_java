package simsgame.activity;

import simsgame.engine.GameContext;
import simsgame.sims.SimCharacter;

/**
 * Eat Activity
 * Reduces the Sim's funds for hunger.
 * Fallback mechanism for Sims who cannot afford a meal.
 */
public class EatActivity extends AbstractActivity {

    private final int mealCost;
    private final int hungerGain;

    /**
     * Constructor with specified costs and gains.
     * @param mealCost Amount deducted from Sim's wallet.
     * @param hungerGain Hunger value restored for meal.
     * @param hours Duration spent eating.
     */
    public EatActivity(int mealCost, int hungerGain, int hours) {
        super("Eat", hours);
        this.mealCost = mealCost;
        this.hungerGain = hungerGain;
    }

    /**
     * Executes eating action.
     * If Sim has enough money, meal cost is deducted and full hunger gain is applied. 
     * If Sim cannot afford, they receive a smaller, free hunger boost.
     * @param sim Sim that is eating.
     * @param context Printing notifications and advancing time.
     */
    @Override
    public void apply(SimCharacter sim, GameContext context) {
        boolean paid = sim.getWallet().spend(mealCost);

        if (paid) {
            sim.getNeeds().addHunger(hungerGain);
            context.notify(sim.getName() + " ate a meal (-$" + mealCost + ", +" + hungerGain + " hunger).");
        } else {
            int freeMealGain = 10; // basic fallback food
            sim.getNeeds().addHunger(freeMealGain);
            context.notify(sim.getName() + " could not afford a meal, but found free food (+" + freeMealGain + " hunger).");
        }
    }
}