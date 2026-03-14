package simsgame.sims;
import java.util.List;
import java.util.ArrayList;

public class Family {
    private List<Child> children = new ArrayList<>(); 
    private boolean pregnant;
    private int pregnancyDay;
    private static final int PREGNANCY_DURATION = 3;

    // Getters
    public List<Child> getChildren(){ return children; }
    public boolean isPregnant(){ return pregnant; }

    public String tryForBaby(String name, int currentDay){
        System.out.printf("%s is trying for a baby!");
        if (Math.random() * 100 < 70) {
            return "The attempt was unsucessful. " + name + " is feeling a bit tired, but there's always next time!";
        }
        else {
            pregnant = true;
            pregnancyDay = currentDay;
            return "Congratulations! " + name + " is pregnant!";
        }
    }

    private void giveBirth(String babyName, int currentDay){
        pregnant = false;
        String gender = null;
        if (Math.random() < 0.5) {
            gender = "Male";
        }
        else { gender = "Female"; }
        Child newChild = new Child(babyName, gender, currentDay);
        children.add(newChild);
    }

    public void onNewDay(int currentDay){
        if (currentDay - pregnancyDay == PREGNANCY_DURATION) {
            giveBirth("John", currentDay);
        }
    }

    public void careforChildren(){
        List<Child> childrenNeedingCare = new ArrayList<>();;
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
                child.updateCareStatus(true);
        }
        }
    }
}
