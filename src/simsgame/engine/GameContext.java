package simsgame.engine;

public interface GameContext {
    void advanceTime(int hours);
    int getCurrentDay();
    int getCurrentHour();
    void notify(String message);
}