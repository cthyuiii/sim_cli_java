package simsgame.sims;
public class Mood {
    public enum moodState {
        HAPPY, OKAY, STRESSED, MISERABLE
    }
    private moodState currentMood;

    public Mood() {
        this.currentMood = moodState.HAPPY;
    }

    public moodState getMoodName() { return currentMood; }

    public void update(Needs needs) {
        int avgNeeds = needs.getAvgStats();
        if (needs.isCritical()) {
            currentMood = moodState.MISERABLE;
        }
        if (avgNeeds < 40) {
            currentMood = moodState.STRESSED;
        }
        else if (avgNeeds < 60) {
            currentMood = moodState.OKAY;
        }
        else {
            currentMood = moodState.HAPPY;
        }
    }
}
