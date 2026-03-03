package simsgame.activity;
import simsgame.*;

public class SocialiseActivity extends AbstractActivity {
    public void apply(SimCharacter sim, Game game) {
        sim.adjustNeed("social", 20);
    }
}
