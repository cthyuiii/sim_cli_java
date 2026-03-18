package simsgame.sims;

import simsgame.activity.Activity;
import simsgame.career.Career;
import simsgame.engine.GameContext;

public class SimCharacter {

    private String name;
    private int age;
    private String gender;
    private int hungerZeroDay = -1;
    private int energyZeroDay = -1;
    private boolean alive = true;

    // Composition (owned by Sim)
    private Needs needs;
    private Mood mood;
    private Wallet wallet;
    private Family family;

    // Aggregation (can change/remove)
    private Career career;

    public SimCharacter(String name, String gender) {
        this.name = name;
        this.age = 18;
        this.gender = gender;
        this.needs = new Needs();
        this.mood = new Mood();
        this.wallet = new Wallet(100); // starting money
        this.family = new Family();
    }

    // =====================================================
    // Core behaviour
    // =====================================================

    public void perform(Activity activity, GameContext context) {
        if (!alive) {
            context.notify(name + " is dead and cannot perform actions.");
            return;
        }
        // Apply activity effects
        activity.apply(this, context);

        // Advance simulation time
        context.advanceTime(activity.hours());

        // Update mood after needs changed
        mood.update(needs);
    }

    // =====================================================
    // Status
    // =====================================================

    public String getStatusString() {

        String careerName = (career == null)
                ? "Unemployed"
                : career.getCareerName();

        return """
                Name: %s
                Age: %d
                Career: %s
                Money: $%.2f

                Needs:
                Hunger: %d
                Energy: %d
                Hygiene: %d
                Social: %d
                Fun: %d

                Mood: %s
               """.formatted(
                name,
                age,
                careerName,
                wallet.getBalance(),
                needs.getHunger(),
                needs.getEnergy(),
                needs.getHygiene(),
                needs.getSocial(),
                needs.getFun(),
                mood.getMoodName()
        );
    }

    public boolean isAlive() {
        return alive;
    }

    public void checkDeath(GameContext context) {

        int day = context.getCurrentDay();

        if (needs.getHunger() <= 0) {
            if (hungerZeroDay == -1) hungerZeroDay = day;
                if (day - hungerZeroDay >= 1) {
                    alive = false;
                    context.notify(name + " has died from starvation...");
                }
            } else {
                hungerZeroDay = -1;
        }

        if (needs.getEnergy() <= 0) {
            if (energyZeroDay == -1) energyZeroDay = day;
                if (day - energyZeroDay >= 1) {
                    alive = false;
                    context.notify(name + " collapsed from exhaustion and died...");
                }
            } else {
                energyZeroDay = -1;
        }
    }

    // =====================================================
    // Getters
    // =====================================================

    public String getName() {
        return name;
    }

    public Needs getNeeds() {
        return needs;
    }

    public String getGender() {
        return gender;
    }

    public Mood getMood() {
        return mood;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public Family getFamily() {
        return family;
    }

    public Career getCareer() {
        return career;
    }

    // =====================================================
    // Career control
    // =====================================================

    public void setCareer(Career career) {
        this.career = career;
    }

    public void removeCareer() {
        this.career = null;
    }
}