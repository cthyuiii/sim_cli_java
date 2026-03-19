package simsgame.sims;

/**
 * Needs Class
 * Manages physical and social requirements of Sim.
 * Handles the calculation of average wellbeing, ensures stat 
 * values remain within valid boundaries
 * Processes the natural decay of needs over time.
 */
public class Needs {
    private int hunger;
    private int energy;
    private int hygiene;
    private int social;
    private int fun;
    
    // Constructor with default values of 80 for all stats
    public Needs() {
        this.hunger = 80;
        this.energy = 80;
        this.hygiene = 80;
        this.social = 80;
        this.fun = 80;
    }

    // Getters
    public int getHunger() { return hunger; }
    public int getEnergy() { return energy; }
    public int getHygiene() { return hygiene; }
    public int getSocial() { return social; }
    public int getFun() { return fun; }

    /**
     * Calculates the overall average of all five core needs.
     * Determine the Sim's emotional state.
     * @return Mean of hunger, energy, hygiene, social, and fun.
     */
    public double getAvgStats() { return (hunger + energy + hygiene + social + fun)/5; }
    public String getStatsStr() {
        return String.format("Hunger: %d | Energy: %d | Hygiene: %d | Social: %d | Fun: %d",
                hunger, energy, hygiene, social, fun);
    }
    
    public void addHunger(int value) { hunger = clamp(hunger + value); }
    public void addEnergy(int value) { energy = clamp(energy + value); }
    public void addHygiene(int value) { hygiene = clamp(hygiene + value); }
    public void addSocial(int value) { social = clamp(social + value); }
    public void addFun(int value) { fun = clamp(fun + value); }

    /**
     * Internal helper method to keep need values within the valid 0-100 range.
     * @param value Raw calculated value before bounding.
     * @return Integer clamped between 0 and 100 inclusive.
     */
    private int clamp(int value) {
        if (value < 0) return 0;
        if (value > 100) return 100;
        return value;
    }

    /**
     * Checks if individual need dropped to dangerous level.
     * Need is considered critical if it is at or below 10.
     * @return true if at least one need is 10 or lower, false otherwise.
     */
    public boolean isCritical() {
        return hunger <= 10 || energy <= 10 || hygiene <= 10 || social <= 10 || fun <= 10;
    }

    /**
     * Passive reduction to all needs based on elapsed time.
     * Different needs decay at different rates per hours.
     * @param hours The number of simulation hours that have passed.
     */
    public void decay(int hours) {
        addHunger(-3 * hours);
        addEnergy(-2 * hours);
        addHygiene(-1 * hours);
        addSocial(-1 * hours);
        addFun(-1 * hours);
    }
}
