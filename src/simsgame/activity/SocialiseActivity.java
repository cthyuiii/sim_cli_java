package simsgame.activity;

import simsgame.engine.GameContext;
import simsgame.sims.SimCharacter;

/**
 * Social interaction activity.
 * Increase both the Social and Fun needs.
 */
public class SocialiseActivity extends AbstractActivity {

    private final int socialGain;
    private final int funGain;

    /**
     * Constructor with specified gains and duration.
     * @param socialGain Social value restored for socialising.
     * @param funGain Fun value restored for socialising.
     * @param hours Duration spent socialising.
     */
    public SocialiseActivity(int socialGain, int funGain, int hours) {
        super("Socialise", hours);
        this.socialGain = socialGain;
        this.funGain = funGain;
    }

    /**
     * Executes socialisation action.
     * Updates the Sim's social and fun levels.
     * @param sim Sim that is socialising
     * @param context Printing notifications and advancing time.
     */
    @Override
    public void apply(SimCharacter sim, GameContext context) {
        sim.getNeeds().addSocial(socialGain);
        sim.getNeeds().addFun(funGain);
        context.notify(sim.getName() + " socialised (+" + socialGain + " social, +" + funGain + " fun).");
    }
}