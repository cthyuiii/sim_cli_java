package simsgame.activity;
import simsgame.*;

public class ShowerActivity extends AbstractActivity {
    public void apply(SimCharacter sim, Game game) {
        sim.adjustNeed("hygiene", 20);
    }
}
