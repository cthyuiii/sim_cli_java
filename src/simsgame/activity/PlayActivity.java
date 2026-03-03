package simsgame.activity;
import simsgame.*;

public class PlayActivity extends AbstractActivity {
    public void apply(SimCharacter sim, Game game) {
        sim.adjustNeed("fun", 20);
    }
}
