public class Mood {
    public enum moodState {
        HAPPY, MISERABLE, STARVING, EXHAUSTED, LONELY
    }
    private moodState currentMood;

    public Mood() {
        this.currentMood = moodState.HAPPY;
    }

    public moodState getMood() { return this.currentMood; }

    public void changeMood(int moodNum) {
        switch (moodNum) {
            case 0:
                this.currentMood = moodState.HAPPY;
                break;
            case 1:
                this.currentMood = moodState.MISERABLE;
                break;
            case 2:
                this.currentMood = moodState.STARVING;
                break;
            case 3:
                this.currentMood = moodState.EXHAUSTED;
                break;
            case 4:
                this.currentMood = moodState.LONELY;
                break;
        }
    }
}
