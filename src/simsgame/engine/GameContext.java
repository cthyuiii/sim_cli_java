package simsgame.engine;

/// GameContext provides necessary game state and interactions for activities and careers.
/// It allows activities to advance time, get current day/hour, and send notifications to the player.
/// This interface is implemented by GameEngine, which manages the overall game state and flow.
public interface GameContext {
    void advanceTime(int hours);
    int getCurrentDay();
    int getCurrentHour();
    void notify(String message);
}