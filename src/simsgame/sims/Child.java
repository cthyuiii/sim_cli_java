package simsgame.sims;

/**
 * Child Class 
 * Tracks development of newborn, including their age,
 * gender, and care requirements as they progress toward adulthood.
 */
public class Child {
    private String name;
    private int ageInDays;
    private String gender;
    private int birthDay;
    private boolean needCare;
    private int daysWithoutCare;

    /**
     * Constructor for new Child instance.
     * Initialises age to 0 and sets the child to require care by default.
     * @param name Baby's name
     * @param gender Gender assigned at birth.
     * @param currentDay Current day representing the birthday.
     */
    public Child(String name, String gender, int currentDay) {
        this.name = name;
        this.ageInDays = 0;
        this.gender = gender;
        this.birthDay = currentDay;
        this.needCare = true;
        this.daysWithoutCare = 0;
    }

    // =====================================================
    // Getters
    // =====================================================
    public String getName() { return name; }
    public int getAgeInDays() { return ageInDays; }
    public String getGender() { return gender; }
    public int getBirthDay() { return birthDay; }
    public boolean getCareStatus() { return needCare; }
    public int getDaysWithoutCare() { return daysWithoutCare; }

    // =====================================================
    // Core behaviour
    // =====================================================
    /**
     * Updates child's name. 
     * Ensures name is not null or empty before applying.
     * @param name New name for child.
     */
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name.trim();
        }
    }

    /**
     * Manually updates the care status of the child.
     * @param care The new care status.
     */
    public void updateCareStatus(boolean care) {
        needCare = care;
        if (!care) {
            daysWithoutCare = 0; // Reset counter when care is provided
        }
    }

    /**
     * Returns formatted string containing relevant child status.
     * @return Status string.
     */
    public String getInfo() {
        return "Name: " + name + 
            ", Age: " + ageInDays + " days" +
            ", Gender: " + gender + 
            ", Birthday: Day " + birthDay + 
            ", Needs Care: " + needCare +
            ", Days Without Care: " + daysWithoutCare;
    }

    /**
     * Updates child's age based on current day.
     * If child reaches 7 days of age, they automatically no longer require care.
     * @param currentDay Current day of the simulation.
     */
    public void updateAge(int currentDay) {
        ageInDays = currentDay - birthDay;

        if (needCare) {
            daysWithoutCare++;
        }
        
        if (ageInDays >= 7) {
            needCare = false;
        }
    }

     public boolean isNeglectedToDeath() {
        return needCare && daysWithoutCare >= 7;
    }

    /**
     * Checks if child reached maturity threshold.
     * Child matures once they reach 14 days of age.
     * @return true if age is 14 or greater, false otherwise.
     */
    public boolean isGrownUp() {
        return ageInDays >= 14;
    }
}
