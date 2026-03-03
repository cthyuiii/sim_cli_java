package simsgame.activity;
import simsgame.Game;
import simsgame.sims.SimCharacter;

public class EatActivity extends AbstractActivity {
    @Override
    public void apply(SimCharacter sim, Game game) {
        sim.adjustNeed("hunger", 20);
    }
}
