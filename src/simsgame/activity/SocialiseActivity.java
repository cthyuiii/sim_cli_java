package simsgame.activity;

import simsgame.engine.GameContext;
import simsgame.sims.SimCharacter;

public class SocialiseActivity extends AbstractActivity {

    private final int socialGain;
    private final int funGain;

    public SocialiseActivity(int socialGain, int funGain, int hours) {
        super("Socialise", hours);
        this.socialGain = socialGain;
        this.funGain = funGain;
    }

    @Override
    public void apply(SimCharacter sim, GameContext context) {
        sim.getNeeds().addSocial(socialGain);
        sim.getNeeds().addFun(funGain);
        context.notify(sim.getName() + " socialised (+" + socialGain + " social, +" + funGain + " fun).");
    }
}