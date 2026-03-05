package simsgame.activity;

import simsgame.engine.GameContext;
import simsgame.sims.SimCharacter;
import simsgame.career.Career;

/**
 * Work delegates work rules to the Sim's Career.
 *
 * IMPORTANT:
 * Prefer Career.work(sim, context) to keep Activities depending on interface only.
 * If your Career currently uses work(sim, GameEngine), change it to use GameContext.
 */
public class WorkActivity implements Activity {

    @Override
    public String name() {
        return "Work";
    }

    /**
     * Work hours usually come from the career type.
     * If no career exists, we use a default.
     */
    @Override
    public int hours() {
        return 8;
    }

    @Override
    public void apply(SimCharacter sim, GameContext context) {
        Career career = sim.getCareer();
        if (career == null) {
            context.notify(sim.getName() + " has no career yet. Assign a job first.");
            return;
        }

        // Recommended design: Career handles pay rules + promotions internally.
        // Expected signature:
        //   int pay = career.work(sim, context);
        //
        // If your Career returns double, cast/round in Career itself.
        double pay = career.work(sim, context);

        // Career may already pay via wallet, but to keep it consistent:
        // Decide ONE approach in your team:
        // Option A) Career returns pay and activity adds to wallet (current approach)
        // Option B) Career directly updates wallet and returns 0 (then remove this)
        if (pay > 0) sim.getWallet().earn(pay);

        context.notify(sim.getName() + " worked as a " + career.getCareerName() + " and earned $" + pay + ".");
    }
}