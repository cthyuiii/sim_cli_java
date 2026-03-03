package simsgame.activity;
import simsgame.Game;
import simsgame.sims.SimCharacter;

public class PlayActivity extends AbstractActivity {
    @Override
    public void apply(SimCharacter sim, Game game) {
        sim.adjustNeed("fun", 20);
    }
}
