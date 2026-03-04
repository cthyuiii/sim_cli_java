package simsgame.activity;
import simsgame.sims.SimCharacter;
import simsgame.Game;

public interface Activity {
    String name();
    int hours();
    void apply(SimCharacter sim, Game game);
}
