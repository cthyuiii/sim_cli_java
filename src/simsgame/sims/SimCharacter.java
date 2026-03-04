package simsgame.sims;

import simsgame.activity.Activity;
import simsgame.engine.GameContext;
import simsgame.career.Career;

public class SimCharacter {

    private String name;
    private int age;

    // Composition (owned by Sim)
    private Needs needs;
    private Mood mood;
    private Wallet wallet;
    private Family family;

    // Aggregation (can change/remove)
    private Career career;

    public SimCharacter(String name) {
        this.name = name;

        this.needs = new Needs();
        this.mood = new Mood();
        this.wallet = new Wallet(100); // starting money
        this.family = new Family();
    }

    // =====================================================
    // Core behaviour
    // =====================================================

    public void perform(Activity activity, GameContext context) {

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
                Money: $%d

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

    // =====================================================
    // Getters
    // =====================================================

    public String getName() {
        return name;
    }

    public Needs getNeeds() {
        return needs;
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