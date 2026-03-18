package simsgame.sims;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    private void giveBirth(String babyName, int currentDay){
        String childName = "Baby" + (children.size() + 1);
        String gender = null;
        if (Math.random() < 0.5) {
            gender = "Male";
        }
        else { gender = "Female"; }
        Child newChild = new Child(babyName, gender, currentDay);
        children.add(newChild);
        pregnant = false;
        pregnancyDay = -1;
        System.out.println("Congratulations! " + babyName + " was born!");
    }

    public void onNewDay(int currentDay){
        if (pregnant && currentDay - pregnancyDay >= PREGNANCY_DURATION) {
            Scanner scanner_obj = new Scanner(System.in);
            System.out.print("Enter a name for the baby: ");
            String babyName = scanner_obj.nextLine();

            giveBirth(babyName, currentDay);
            scanner_obj.close();
        }

        for (Child child : children) {
            child.updateAge(currentDay);
        }
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
