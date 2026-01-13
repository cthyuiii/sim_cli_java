import java.util.Random;

public class SimCharacter {
    private String name;
    private int hunger;        // 0-100 (100 = full, 0 = starving)
    private int energy;        // 0-100 (100 = well rested, 0 = exhausted)
    private int hygiene;       // 0-100 (100 = clean, 0 = dirty)
    private int social;        // 0-100 (100 = fulfilled, 0 = lonely)
    private int fun;           // 0-100 (100 = entertained, 0 = bored)
    private int money;
    private int currentHour;   // 0-23 (24-hour format)
    private int currentDay;
    private boolean isAlive;
    private Random random;

    public SimCharacter(String name) {
        this.name = name;
        this.hunger = 80;
        this.energy = 80;
        this.hygiene = 80;
        this.social = 80;
        this.fun = 80;
        this.money = 500;
        this.currentHour = 8;  // Start at 8 AM
        this.currentDay = 1;
        this.isAlive = true;
        this.random = new Random();
    }

    // Getters
    public String getName() { return name; }
    public int getHunger() { return hunger; }
    public int getEnergy() { return energy; }
    public int getHygiene() { return hygiene; }
    public int getSocial() { return social; }
    public int getFun() { return fun; }
    public int getMoney() { return money; }
    public int getCurrentHour() { return currentHour; }
    public int getCurrentDay() { return currentDay; }
    public boolean isAlive() { return isAlive; }

    // Time management
    public void passTime(int hours) {
        currentHour += hours;
        while (currentHour >= 24) {
            currentHour -= 24;
            currentDay++;
        }
        
        // Needs degrade over time
        hunger = Math.max(0, hunger - (hours * 5));
        energy = Math.max(0, energy - (hours * 3));
        hygiene = Math.max(0, hygiene - (hours * 2));
        social = Math.max(0, social - (hours * 2));
        fun = Math.max(0, fun - (hours * 2));
        
        checkVitals();
    }

    // Activities
    public void eat(String mealType) {
        switch (mealType.toLowerCase()) {
            case "snack":
                if (money >= 5) {
                    hunger = Math.min(100, hunger + 15);
                    money -= 5;
                    passTime(1);
                    System.out.println(name + " ate a snack. Hunger increased!");
                } else {
                    System.out.println("Not enough money for a snack!");
                }
                break;
            case "meal":
                if (money >= 15) {
                    hunger = Math.min(100, hunger + 40);
                    fun = Math.min(100, fun + 5);
                    money -= 15;
                    passTime(2);
                    System.out.println(name + " ate a delicious meal. Feeling satisfied!");
                } else {
                    System.out.println("Not enough money for a meal!");
                }
                break;
            case "feast":
                if (money >= 30) {
                    hunger = Math.min(100, hunger + 70);
                    fun = Math.min(100, fun + 15);
                    social = Math.min(100, social + 10);
                    money -= 30;
                    passTime(3);
                    System.out.println(name + " enjoyed a feast! Multiple needs satisfied!");
                } else {
                    System.out.println("Not enough money for a feast!");
                }
                break;
        }
    }

    public void sleep(int hours) {
        if (hours < 1 || hours > 12) {
            System.out.println("Please sleep between 1 and 12 hours.");
            return;
        }
        energy = Math.min(100, energy + (hours * 15));
        hunger = Math.max(0, hunger - (hours * 3));
        passTime(hours);
        System.out.println(name + " slept for " + hours + " hours. Feeling refreshed!");
    }

    public void shower() {
        hygiene = Math.min(100, hygiene + 50);
        energy = Math.max(0, energy - 5);
        passTime(1);
        System.out.println(name + " took a shower. Feeling clean!");
    }

    public void socialize() {
        social = Math.min(100, social + 30);
        fun = Math.min(100, fun + 15);
        energy = Math.max(0, energy - 10);
        passTime(2);
        System.out.println(name + " spent time with friends. Feeling connected!");
    }

    public void playGame() {
        fun = Math.min(100, fun + 35);
        energy = Math.max(0, energy - 10);
        passTime(2);
        System.out.println(name + " played video games. Having fun!");
    }

    public void watchTV() {
        fun = Math.min(100, fun + 20);
        energy = Math.max(0, energy - 5);
        passTime(2);
        System.out.println(name + " watched TV. Feeling entertained!");
    }

    public void work() {
        if (energy < 30) {
            System.out.println(name + " is too tired to work!");
            return;
        }
        int earnings = 50 + random.nextInt(51); // $50-$100
        money += earnings;
        energy = Math.max(0, energy - 25);
        fun = Math.max(0, fun - 10);
        hunger = Math.max(0, hunger - 15);
        passTime(4);
        System.out.println(name + " worked hard and earned $" + earnings + "!");
    }

    public void exercise() {
        if (energy < 15) {
            System.out.println(name + " is too tired to exercise!");
            return;
        }
        energy = Math.max(0, energy - 20);
        hunger = Math.max(0, hunger - 15);
        fun = Math.min(100, fun + 10);
        passTime(2);
        System.out.println(name + " exercised. Feeling accomplished!");
    }

    // Check if character is still alive
    private void checkVitals() {
        if (hunger <= 0 || energy <= 0) {
            isAlive = false;
            System.out.println("\n*** GAME OVER ***");
            if (hunger <= 0) {
                System.out.println(name + " has died from starvation!");
            } else if (energy <= 0) {
                System.out.println(name + " has died from exhaustion!");
            }
        }
    }

    // Display character status
    public void displayStatus() {
        System.out.println("\n========================================");
        System.out.println("  " + name + "'s Status");
        System.out.println("========================================");
        System.out.println("Day: " + currentDay + " | Time: " + formatTime());
        System.out.println("Money: $" + money);
        System.out.println("----------------------------------------");
        System.out.println("Hunger:  " + getBar(hunger) + " " + hunger + "%");
        System.out.println("Energy:  " + getBar(energy) + " " + energy + "%");
        System.out.println("Hygiene: " + getBar(hygiene) + " " + hygiene + "%");
        System.out.println("Social:  " + getBar(social) + " " + social + "%");
        System.out.println("Fun:     " + getBar(fun) + " " + fun + "%");
        System.out.println("========================================\n");
        
        // Warnings
        if (hunger < 30) System.out.println("⚠ WARNING: " + name + " is very hungry!");
        if (energy < 30) System.out.println("⚠ WARNING: " + name + " is exhausted!");
        if (hygiene < 30) System.out.println("⚠ WARNING: " + name + " needs a shower!");
        if (social < 30) System.out.println("⚠ WARNING: " + name + " is feeling lonely!");
        if (fun < 30) System.out.println("⚠ WARNING: " + name + " is bored!");
        System.out.println();
    }

    private String formatTime() {
        int displayHour = currentHour;
        String period = "AM";
        if (displayHour >= 12) {
            period = "PM";
            if (displayHour > 12) displayHour -= 12;
        }
        if (displayHour == 0) displayHour = 12;
        return displayHour + ":00 " + period;
    }

    private String getBar(int value) {
        int bars = value / 10;
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < 10; i++) {
            if (i < bars) {
                bar.append("█");
            } else {
                bar.append("░");
            }
        }
        bar.append("]");
        return bar.toString();
    }
}
