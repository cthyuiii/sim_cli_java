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
    public Needs getNeeds() { return this.needs; }
    public Mood getMood() { return this.mood; }
    public Wallet getWallet() { return this.wallet; }
    public Family getFamily() { return this.family; }
    public Career getCareer() { return this.career; }

    public void getStatusStr() { 
        System.out.println(needs.getStatsStr());
    }

    public void updateMood() {
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

    
}