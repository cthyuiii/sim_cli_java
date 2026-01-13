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
    private String currentMood;
    private Career career;
    private double moodMultiplier; // Affects how fast needs degrade

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
        this.career = null;
        updateMood();
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
    public String getCurrentMood() { return currentMood; }
    public Career getCareer() { return career; }
    public boolean hasCareer() { return career != null; }

    public void setCareer(Career career) {
        this.career = career;
    }

    // Mood calculation based on all needs
    private void updateMood() {
        int averageNeeds = (hunger + energy + hygiene + social + fun) / 5;
        
        // Special case: Icky mood when hygiene is critically low
        if (hygiene < 20) {
            currentMood = "Icky";
            moodMultiplier = 1.8; // Needs degrade 80% faster
            return;
        }
        
        // Calculate mood based on average needs
        if (averageNeeds >= 80) {
            currentMood = "Ecstatic";
            moodMultiplier = 0.7; // Needs degrade 30% slower
        } else if (averageNeeds >= 60) {
            currentMood = "Happy";
            moodMultiplier = 0.9; // Needs degrade 10% slower
        } else if (averageNeeds >= 40) {
            currentMood = "Content";
            moodMultiplier = 1.0; // Normal degradation
        } else if (averageNeeds >= 25) {
            currentMood = "Uncomfortable";
            moodMultiplier = 1.3; // Needs degrade 30% faster
        } else {
            currentMood = "Miserable";
            moodMultiplier = 1.6; // Needs degrade 60% faster
        }
    }

    // Get mood emoji
    private String getMoodEmoji() {
        switch (currentMood) {
            case "Ecstatic": return "😄";
            case "Happy": return "😊";
            case "Content": return "🙂";
            case "Uncomfortable": return "😟";
            case "Miserable": return "😢";
            case "Icky": return "🤢";
            default: return "😐";
        }
    }

    // Time management with mood-affected degradation
    public void passTime(int hours) {
        currentHour += hours;
        while (currentHour >= 24) {
            currentHour -= 24;
            currentDay++;
        }
        
        // Needs degrade over time, affected by mood
        hunger = Math.max(0, hunger - (int)(hours * 5 * moodMultiplier));
        energy = Math.max(0, energy - (int)(hours * 3 * moodMultiplier));
        hygiene = Math.max(0, hygiene - (int)(hours * 2 * moodMultiplier));
        social = Math.max(0, social - (int)(hours * 2 * moodMultiplier));
        fun = Math.max(0, fun - (int)(hours * 2 * moodMultiplier));
        
        // Special effect: Icky mood drains energy faster
        if (currentMood.equals("Icky")) {
            energy = Math.max(0, energy - (hours * 5));
        }
        
        updateMood();
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
        
        // Special message if recovering from Icky mood
        if (hygiene >= 40) {
            System.out.println("Much better! No longer feeling icky.");
        }
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
        if (career == null) {
            System.out.println("You need to choose a career first!");
            return;
        }
        
        int requiredEnergy = 30 + (career.getWorkHours() - 4) * 5; // More energy for longer shifts
        if (energy < requiredEnergy) {
            System.out.println(name + " is too tired to work! (Need " + requiredEnergy + "+ energy)");
            return;
        }
        
        int earnings = career.work();
        money += earnings;
        energy = Math.max(0, energy - (career.getWorkHours() * 5));
        fun = Math.max(0, fun - 10);
        hunger = Math.max(0, hunger - (career.getWorkHours() * 3));
        passTime(career.getWorkHours());
        System.out.println(name + " worked as a " + career.getCurrentTitle() + " and earned $" + earnings + "!");
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
        System.out.println("Mood: " + getMoodEmoji() + " " + currentMood);
        System.out.println("----------------------------------------");
        System.out.println("Hunger:  " + getBar(hunger) + " " + hunger + "%");
        System.out.println("Energy:  " + getBar(energy) + " " + energy + "%");
        System.out.println("Hygiene: " + getBar(hygiene) + " " + hygiene + "%");
        System.out.println("Social:  " + getBar(social) + " " + social + "%");
        System.out.println("Fun:     " + getBar(fun) + " " + fun + "%");
        System.out.println("========================================");
        
        if (career != null) {
            System.out.println("\n--- Career Info ---");
            System.out.println(career.getCareerInfo());
            System.out.println("========================================");
        }
        System.out.println();
        
        // Warnings
        if (currentMood.equals("Icky")) {
            System.out.println("🤢 WARNING: Feeling ICKY! Hygiene is terrible and energy is draining fast!");
        }
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
