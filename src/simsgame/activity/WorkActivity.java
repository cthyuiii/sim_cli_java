package simsgame.activity;

import simsgame.career.Career;
import simsgame.engine.GameContext;
import simsgame.sims.SimCharacter;

public class WorkActivity extends AbstractActivity {

    public WorkActivity() {
        super("Work", 0); // real hours come from Career
    }

    @Override
    public void apply(SimCharacter sim, GameContext context) {
        Career career = sim.getCareer();
        if (career == null) {
            context.notify(sim.getName() + " has no career yet. Assign a job first.");
            return;
        }

        double pay = career.work(sim, context);

        if (pay > 0) {
            sim.getWallet().earn((int) Math.round(pay));
        }

        context.notify(sim.getName() + " worked as a " +
                career.getCareerName() + " and earned $" + pay + ".");
    }
}