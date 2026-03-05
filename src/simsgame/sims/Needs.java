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
    public int getAvgStats() { return (hunger + energy + hygiene + social + fun)/5; }
    public String getStatsStr() { return String.format("(\"Hunger: %d | Energy: %d | Hygiene: %d | Social: %d | Fun: %d\", )", this.hunger, this.energy, this.hygiene, this.social, this.fun); }

    public void addHunger(int value) { Math.min(100, hunger += value); }
    public void addEnergy(int value) { Math.min(100, energy += value); }
    public void addHygiene(int value) { Math.min(100, hygiene += value); }
    public void addSocial(int value) { Math.min(100, social += value); }
    public void addFun(int value) { Math.min(100, fun += value); }

    public void decay(int hours) {
        hunger -= 3 * (hours);
        energy -= 2 * (hours);
        hygiene -= (hours);
        social -= (hours);
        fun -= (hours);

        // Missing prompting when falling below threshold 
    }
}
