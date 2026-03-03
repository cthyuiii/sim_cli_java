package simsgame.activity;
import simsgame.Game;
import simsgame.sims.SimCharacter;

public class ShowerActivity extends AbstractActivity {
    @Override
    public void apply(SimCharacter sim, Game game) {
        sim.adjustNeed("hygiene", 20);
    }
}
