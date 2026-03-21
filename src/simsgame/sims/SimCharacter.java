package simsgame.sims;

import simsgame.activity.Activity;
import simsgame.career.Career;
import simsgame.engine.GameContext;

public class SimCharacter {

    // =====================================================
    // Attributes
    // =====================================================
    private String name;
    private int age;
    private String gender;
    private int hungerZeroDay = -1;
    private int energyZeroDay = -1;
    private boolean alive = true;
    private boolean busy;
    private int remainingBusyHours;

    // =====================================================
    // Composition
    // =====================================================
    private Needs needs;
    private Mood mood;
    private Wallet wallet;
    private Family family;

   // =====================================================
    // Aggregation
    // =====================================================
    private Career career;

    /**
     * Constructs SimCharacter with name and gender.
     * Initialises the Sim at age 18 with starting funds and default needs.
     * @param name Name for new Sim.
     * @param gender Gender for new Sim.
     */
    public SimCharacter(String name, String gender) {
        this.name = name;
        this.age = 18;
        this.gender = gender;
        this.needs = new Needs();
        this.mood = new Mood();
        this.wallet = new Wallet(100); // starting money
        this.family = new Family();
        this.busy = false;
        this.remainingBusyHours = 0;
    }

    // =====================================================
    // Getters
    // =====================================================
    public String getName() { return name; }
    public Needs getNeeds() { return needs; }
    public String getGender() { return gender; }
    public Mood getMood() { return mood; }
    public Wallet getWallet() { return wallet; }
    public Family getFamily() { return family; }
    public Career getCareer() { return career; }
    public boolean isAlive() { return alive; }

    // =====================================================
    // Core behaviour
    // =====================================================
    /**
     * Sim to perform specific activity.
     * If alive, Sim applies the activity's effects, advances game time, 
     * and updates their mood based on the resulting need changes.
     * @param activity Action to be performed.
     * @param context Printing notifications and advancing time.
     */
    public void perform(Activity activity, GameContext context) {
        if (!alive) {
            context.notify(name + " is dead and cannot perform actions.");
            return;
        }
        if (busy) {
            context.notify(name + " is busy for " + remainingBusyHours + " more hour(s).");
            return;
        }
        // Apply activity effects
        activity.apply(this, context);

        // Advance simulation time
        //context.advanceTime(activity.hours());
        int duration = activity.hours();
        if (duration > 0) {
            assignBusyTime(duration);
        }

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

        String pregnancyStatus = family.isPregnant() ? "Pregnant" : "Not Pregnant";

        return """
                Name: %s
                Age: %d
                Gender: %s
                Career: %s
                Money: $%.2f
                Pregnancy Status: %s

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
                gender,
                careerName,
                wallet.getBalance(),
                pregnancyStatus,
                needs.getHunger(),
                needs.getEnergy(),
                needs.getHygiene(),
                needs.getSocial(),
                needs.getFun(),
                mood.getMoodName()
        );
    }

    /**
     * Evaluates whether Sim has died from critical neglect.
     * Sim dies if hunger or energy remains at zero for one full day.
     * @param context Printing notifications and advancing time.
     */
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
    // Busy status control
    // =====================================================

    public boolean isBusy() {
        return busy;
    }

    public int getRemainingBusyHours() {
        return remainingBusyHours;
    }

    public void assignBusyTime(int hours) {
        if (hours > 0) {
            this.busy = true;
            this.remainingBusyHours = hours;
        }
    }

    public void reduceBusyTime(int hours) {
        if (!busy) return;

        remainingBusyHours -= hours;

        if (remainingBusyHours <= 0) {
            remainingBusyHours = 0;
            busy = false;
        }
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