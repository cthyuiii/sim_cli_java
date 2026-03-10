package simsgame.sims;
import java.util.List;
import java.util.ArrayList;

public class Family {
    // WAIT IDK WHAT TYPE THIS IS
    List<Child> children = new ArrayList<Child>(); 
    private boolean pregnant;
    private int pregnancyDays;
    private static final int PREGNANCY_DURATION = 3;

    public void tryforBaby(){

    }

    private void giveBirth(int currentDay){

    }

    public void advanceDay(int currentDay){

    }

    public void careforChildren(){

    }

    // Getters
    public List<Child> getChildren(){
        return children;
    }

    public boolean isPregnant(){
        return true;
    }

}
