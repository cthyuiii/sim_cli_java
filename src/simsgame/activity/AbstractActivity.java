package simsgame.activity;

import simsgame.Game;
import simsgame.SimCharacter;

public abstract class AbstractActivity {
    public abstract void apply(SimCharacter sim, Game game);
}
