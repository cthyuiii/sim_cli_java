package simsgame.sims;
import simsgame.career.*;

public class SimCharacter {
    private String name;
    private Needs needs;
    private Mood mood;
    private Wallet wallet;
    private Family family;
    private Career career;

    public SimCharacter(String name) {
        this.name = name;
        this.needs = new Needs();
        this.mood = new Mood();
        this.wallet = new Wallet();
        this.family = new Family();
        this.career = new Career();
    }

    public String getName() { return this.name; }

    public void calculateMood() {
        int moodNum = 0;
        if (needs.getAvgStats() < 25) {
            moodNum = 1;
        }
        else if (needs.getHunger() < 20) {
            moodNum = 2;
        }
        else if (needs.getEnergy() < 20) {
            moodNum = 3;
        }
        else if (needs.getSocial() < 20 && needs.getFun() < 20) {
            moodNum = 4;
        }
        mood.changeMood(moodNum);
    }

    public void getMood() {
        System.out.println(mood.getMood());
    }

    public void adjustNeed(String needName, int amount) {
        switch (needName) {
            case "hunger":
                this.needs.addHunger(amount);
                break;
            case "energy":
                this.needs.addEnergy(amount);
                break;
            case "hygiene":
                this.needs.addHygiene(amount);
                break;
            case "social":
                this.needs.addSocial(amount);
                break;
            case "fun":
                this.needs.addFun(amount);
                break;
        }
    }
}