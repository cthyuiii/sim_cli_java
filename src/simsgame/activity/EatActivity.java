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
            int freeMealGain = 10; // basic fallback food
            sim.getNeeds().addHunger(freeMealGain);
            context.notify(sim.getName() + " could not afford a meal, but found free food (+" + freeMealGain + " hunger).");
        }
    }
}