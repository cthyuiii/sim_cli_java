package simsgame.activity;
import simsgame.*;

public class EatActivity extends AbstractActivity {
    public void apply(SimCharacter sim, Game game) {
        sim.adjustNeed("hunger", 20);
    }
}
