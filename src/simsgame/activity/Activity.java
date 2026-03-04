package simsgame.activity;

import simsgame.engine.GameContext;
import simsgame.sims.SimCharacter;

public interface Activity {
    String name();
    int hours();
    void apply(SimCharacter sim, GameContext context);
}