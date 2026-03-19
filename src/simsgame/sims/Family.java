package simsgame.sims;
import java.util.ArrayList;
import java.util.List;

/**
 * Family Class
 * Handles pregnancy tracking, childbirth, and the care 
 * or growth of children within a household.
 */
public class Family {
    private List<Child> children = new ArrayList<>(); 
    private boolean pregnant;
    private int pregnancyDay;
    private static final int PREGNANCY_DURATION = 3;

    /**
     * Constructor for new Family unit.
     * Initialises an empty list of children 
     * Sets pregnancy status to false.
     */
    public Family() {
        this.children = new ArrayList<>();
        this.pregnant = false;
        this.pregnancyDay = -1;
    }

    // =====================================================
    // Getters
    // =====================================================
    public List<Child> getChildren(){ return children; }
    public boolean isPregnant(){ return pregnant; }

    // =====================================================
    // Core behaviour
    // =====================================================
    /**
     * Filters the children list for those who have matured.
     * @return List of Child objects where isGrownUp() is true.
     */
    public List<Child> getGrownUpChildren() {
        List<Child> grownUps = new ArrayList<>();
        for (Child child : children) {
            if (child.isGrownUp()) {
                grownUps.add(child);
            }
        }
        return grownUps;
    }

    /**
     * Removes a child from the family, typically when they grow up.
     * @param child Child object to remove.
     */
    public void removeChild(Child child) {
        children.remove(child);
    }

    /**
     * Attempt for pregnancy for Sim.
     * 30% success rate per attempt.
     * @param name Sim trying for a baby.
     * @param currentDay Current day.
     * @return Message string indicating success or failure.
     */
    public String tryForBaby(String name, int currentDay){
        if (pregnant) {
            return name + " is already pregnant and cannot try for another baby yet.";
        }

        System.out.printf("%s is trying for a baby!", name);

        if (Math.random() * 100 < 70) {
            return "The attempt was unsuccessful. " + name + " is feeling a bit tired, but there's always next time!";
        }
        else {
            pregnant = true;
            pregnancyDay = currentDay;
            return "Congratulations! " + name + " is pregnant!";
        }
    }

    /**
     * Internal logic to generate new child.
     * @param currentDay Birthday of the child.
     * @return Newly created Child object.
     */
    private Child giveBirth(int currentDay){
        pregnant = false;
        pregnancyDay = -1;

        String gender = (Math.random() < 0.5) ? "Male" : "Female";
        Child newChild = new Child("Baby", gender, currentDay);
        children.add(newChild);
        return newChild;
    }

    /**
     * Daily updates for family. 
     * Handles childbirth if pregnancy duration is met and updates child ages.
     * @param currentDay Current day.
     * @return List of any newborns born on this day.
     */
    public List<Child> onNewDay(int currentDay) {
        List<Child> newborns = new ArrayList<>();

        if (pregnant && currentDay - pregnancyDay >= PREGNANCY_DURATION) {
            Child baby = giveBirth(currentDay);
            newborns.add(baby);
        }

        for (Child child : children) {
            child.updateAge(currentDay);
        }

        return newborns;
    }

    /**
     * Fulfills care requirements for all children currently needing it.
     */
    public void careForChildren(){
        List<Child> childrenNeedingCare = new ArrayList<>();
        for (Child child : children) {
            if (child.getCareStatus()) {
                childrenNeedingCare.add(child);
            }
        }

        if (childrenNeedingCare.isEmpty()) {
            System.out.println("No child needs care at the moment.");
        }
        else {
            for (Child child: childrenNeedingCare) {
                child.updateCareStatus(false);
        }
        }
    }
}
