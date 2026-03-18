package simsgame.sims;
import java.util.ArrayList;
import java.util.List;

public class Family {
    private List<Child> children = new ArrayList<>(); 
    private boolean pregnant;
    private int pregnancyDay;
    private static final int PREGNANCY_DURATION = 3;

    // Getters
    public List<Child> getChildren(){ return children; }
    public boolean isPregnant(){ return pregnant; }

    public List<Child> getGrownUpChildren() {
        List<Child> grownUps = new ArrayList<>();
        for (Child child : children) {
            if (child.isGrownUp()) {
                grownUps.add(child);
            }
        }
        return grownUps;
    }

    public void removeChild(Child child) {
        children.remove(child);
    }

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

    private Child giveBirth(int currentDay){
        pregnant = false;
        pregnancyDay = -1;

        String gender = (Math.random() < 0.5) ? "Male" : "Female";
        Child newChild = new Child("Baby", gender, currentDay);
        children.add(newChild);
        return newChild;
    }

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
