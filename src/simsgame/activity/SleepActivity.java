package simsgame.activity;
import simsgame.*;

public class SleepActivity extends AbstractActivity {
    public void apply(SimCharacter sim, Game game) {
        sim.adjustNeed("energy", 20);
    }
}
