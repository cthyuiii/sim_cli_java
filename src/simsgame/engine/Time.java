package simsgame.engine;

public class Time {

    private int currentDay;
    private int currentHour;

    public Time() {
        reset();
    }

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

    public int getCurrentDay() {
        return currentDay;
    }

    public int getCurrentHour() {
        return currentHour;
    }
}