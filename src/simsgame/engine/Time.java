package simsgame.engine;
// Time class manages the in-game time, tracking current day and hour, and allowing time advancement.
// Time starts at Day 1, Hour 8 (8 AM) and advances based on activities and careers.
// It provides methods to get the current day and hour, and to reset time when starting a new game.

public class Time {

    // =====================================================
    // Fields
    // =====================================================

    private int currentDay;
    private int currentHour;

    public Time() {
        reset();
    }

    // =====================================================
    // Core behaviour
    // =====================================================

    public void reset() {
        currentDay = 1;
        currentHour = 8;
    }

    public int advance(int hours) {
        currentHour += hours;
        int daysPassed = 0;

        while (currentHour >= 24) {
            currentHour -= 24;
            currentDay++;
            daysPassed++;
        }

        return daysPassed;
    }

    // =====================================================
    // Getters
    // =====================================================

    public int getCurrentDay() {
        return currentDay;
    }

    public int getCurrentHour() {
        return currentHour;
    }
}