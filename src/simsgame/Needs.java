package simsgame;
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

    // Overloaded for custom character creation stats
    public Needs(int hunger, int energy, int hygiene, int social, int fun) {
        this.hunger = hunger;
        this.energy = energy;
        this.hygiene = hygiene;
        this.social = social;
        this.fun = fun;
    }

    // Getters
    public int getHunger() { return hunger; }
    public int getEnergy() { return energy; }
    public int getHygiene() { return hygiene; }
    public int getSocial() { return social; }
    public int getFun() { return fun; }
    public int getAvgStats() { return (hunger + energy + hygiene + social + fun)/5; }
    public void showStats() { 
        String statsStr = String.format("Hunger: %d | Energy: %d | Hygiene: %d | Social: %d | Fun: %d"); 
        System.out.println(statsStr);
    }
}
