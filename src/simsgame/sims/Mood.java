package simsgame.sims;

/**
 * Mood Class
 * Emotional state of Sim.
 * Mood is derived from calculating based on the average needs of Sim 
 * with critical needs taking precedence.
 */
public class Mood {
    public enum moodState {
        HAPPY, OKAY, STRESSED, MISERABLE
    }
    private moodState currentMood;

    /**
     * Constructor for new Mood instance.
     * By default, Sim starts in HAPPY state.
     */
    public Mood() {
        this.currentMood = moodState.HAPPY;
    }

    public moodState getMoodName() { return currentMood; }

    /**
     * Updates the current mood based on the provided needs.
     * 1. If any need is critical (<= 10), mood is MISERABLE.
     * 2. If average needs are below 40, mood is STRESSED.
     * 3. If average needs are below 60, mood is OKAY.
     * 4. Else, mood is HAPPY.
     * * @param needs Needs object containing Sim's current stats.
     */
    public void update(Needs needs) {
        double avgNeeds = needs.getAvgStats();
        if (needs.isCritical()) {
            currentMood = moodState.MISERABLE;
        } else if (avgNeeds < 40) {
            currentMood = moodState.STRESSED;
        } else if (avgNeeds < 60) {
            currentMood = moodState.OKAY;
        } else {
            currentMood = moodState.HAPPY;
        }
    }
}
