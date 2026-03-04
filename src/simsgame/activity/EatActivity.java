package simsgame.activity;

import simsgame.engine.GameContext;
import simsgame.sims.SimCharacter;

public class EatActivity extends AbstractActivity {

    private final int mealCost;
    private final int hungerGain;

    public EatActivity(int mealCost, int hungerGain, int hours) {
        super("Eat", hours);
        this.mealCost = mealCost;
        this.hungerGain = hungerGain;
    }

    @Override
    public void apply(SimCharacter sim, GameContext context) {
        boolean paid = sim.getWallet().spend(mealCost);

        if (paid) {
            sim.getNeeds().addHunger(hungerGain);
            context.notify(sim.getName() + " ate a meal (-$" + mealCost + ", +" + hungerGain + " hunger).");
        } else {
            // fallback if broke
            int smallGain = Math.max(5, hungerGain / 3);
            sim.getNeeds().addHunger(smallGain);
            context.notify(sim.getName() + " couldn't afford a full meal, ate a cheap snack (+" + smallGain + " hunger).");
        }
    }
}