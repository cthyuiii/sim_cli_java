package simsgame.sims;
public class Needs {
    private int hunger;
    private int energy;
    private int hygiene;
    private int social;
    private int fun;
    
    // Default Constructor
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

    private int clamp(int value) {
        if (value < 0) return 0;
        if (value > 100) return 100;
        return value;
    }

    public boolean isCritical() {
        return hunger <= 10 || energy <= 10 || hygiene <= 10 || social <= 10 || fun <= 10;
    }

    public void decay(int hours) {
        addHunger(-3 * hours);
        addEnergy(-2 * hours);
        addHygiene(-1 * hours);
        addSocial(-1 * hours);
        addFun(-1 * hours);
    }
}
