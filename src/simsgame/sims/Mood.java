package simsgame.sims;
public class Mood {
    public enum moodState {
        HAPPY, OKAY, STRESSED
    }
    private moodState currentMood;

    public Mood() {
        this.currentMood = moodState.HAPPY;
    }

    public moodState getMoodName() { return this.currentMood; }

    public void update(Needs needs) {
        int avgNeeds = needs.getAvgStats();
        if (avgNeeds < 25) {
            this.currentMood = moodState.STRESSED;
        }
        else if (avgNeeds < 50) {
            currentMood = moodState.OKAY;
        }
        else {
            currentMood = moodState.HAPPY;
        }
    }
}
