package simsgame.activity;
import simsgame.Game;
import simsgame.sims.SimCharacter;

public class SleepActivity extends AbstractActivity {
    @Override
    public void apply(SimCharacter sim, Game game) {
        sim.adjustNeed("energy", 20);
    }
}
