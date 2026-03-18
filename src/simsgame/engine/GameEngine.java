package simsgame.engine;

import java.util.ArrayList;
import java.util.List;
import simsgame.activity.Activity;
import simsgame.career.Career;
import simsgame.career.CareerFactory;
import simsgame.sims.Child;
import simsgame.sims.SimCharacter;

/**
 * GameEngine handles:
 * - Simulation state
 * - Time progression
 * - Global updates
 * - Activity execution
 * 
 * It does NOT handle UI.
 */
public class GameEngine implements GameContext {

    private List<SimCharacter> sims;
    private int activeSimIndex;
    private List<Child> pendingNewborns;

    private Time time;
    private ActivityRegistry activityRegistry;

    public GameEngine() {
        sims = new ArrayList<>();
        pendingNewborns = new ArrayList<>();
        activeSimIndex = -1;
        time = new Time();
        activityRegistry = new ActivityRegistry();
    }

    // =====================================================
    // Game Lifecycle
    // =====================================================

    public void startNewGame() {
        sims.clear();
        pendingNewborns.clear();
        activeSimIndex = -1;
        time.reset();
    }

    // =====================================================
    // Sim Management
    // =====================================================

    public void createSim(String name, String gender) {
        SimCharacter sim = new SimCharacter(name, gender);
        sims.add(sim);

        if (activeSimIndex == -1) {
            activeSimIndex = 0;
        }
    }

    public int getSimCount() {
        return sims.size();
    }

    public List<String> getSimNames() {
        List<String> names = new ArrayList<>();
        for (SimCharacter sim : sims) {
            names.add(sim.getName());
        }
        return names;
    }

    public int getActiveSimIndex() {
        return activeSimIndex;
    }

    public void setActiveSimIndex(int index) {
        if (index >= 0 && index < sims.size()) {
            activeSimIndex = index;
        }
    }

    public SimCharacter getActiveSim() {
        if (activeSimIndex < 0 || activeSimIndex >= sims.size()) {
            return null;
        }
        return sims.get(activeSimIndex);
    }

    // =====================================================
    // Status
    // =====================================================

    public String getActiveSimStatus() {
        SimCharacter sim = getActiveSim();
        if (sim == null) return "No active Sim.";

        return "Day " + time.getCurrentDay() +
               ", Hour " + time.getCurrentHour() + "\n" +
               sim.getStatusString();
    }

    // =====================================================
    // Activities
    // =====================================================

    public List<String> getActivityNames() {
        List<Activity> activities = activityRegistry.getAllActivities();
        List<String> names = new ArrayList<>();

        for (Activity a : activities) {
            names.add(a.name() + " (" + a.hours() + "h)");
        }

        return names;
    }

    public String performActivity(int activityIndex) {
        SimCharacter sim = getActiveSim();
        if (sim == null) return "No active Sim.";

        Activity activity = activityRegistry.getActivityByIndex(activityIndex);
        if (activity == null) return "Invalid activity.";

        // Delegation
        sim.perform(activity, this);

        return sim.getName() + " performed: " + activity.name();
    }

    // =====================================================
    // Career
    // =====================================================

    public List<String> getCareerOptions() {
        return CareerFactory.getCareerTypes();
    }   

    public String assignCareerToActiveSim(int type, String title) {
        SimCharacter sim = getActiveSim();
        if (sim == null) return "No active Sim.";

        Career career = CareerFactory.createCareer(type, title);
        if (career == null) return "Invalid career type.";

        sim.setCareer(career);
        return sim.getName() + " is now a " + career.getCareerName() + ".";
    }

    // =====================================================
    // Family 
    // =====================================================

    public String tryForBabyActiveSim() {
        SimCharacter sim = getActiveSim();
        if (sim == null) return "No active Sim.";

        if (!sim.getGender().equalsIgnoreCase("Female")) {
            return sim.getName() + " cannot become pregnant.";
        }

        if (sim.getFamily().isPregnant()) {
            return sim.getName() + " is already pregnant.";
        }

        if (sim.getWallet().getBalance() < 100)  { 
            return "You have insufficient funds to raise a baby!"; 
        }
        
        if (sim.getNeeds().getSocial() < 50 || sim.getNeeds().getEnergy() < 40) { 
            return "You do not feel like trying for a child at the moment.."; 
        }

        return sim.getFamily().tryForBaby(sim.getName(), time.getCurrentDay());
    }
    public boolean hasPendingNewborns() {
        return !pendingNewborns.isEmpty();
    }

    public List<Child> getPendingNewborns() {
        return pendingNewborns;
    }

    public void nameNewborn(Child child, String name) {
        if (child != null) {
            child.setName(name);
        }
    }

    // =====================================================
    // Time Handling
    // =====================================================

    @Override
    public void advanceTime(int hours) {
        int previousDay = time.getCurrentDay();
        int daysPassed = time.advance(hours);

        // Hourly updates
        for (SimCharacter sim : sims) {

            if (!sim.isAlive()) continue;

            sim.getNeeds().decay(hours);
            sim.getMood().update(sim.getNeeds());
            sim.checkDeath(this);
        }
        List<SimCharacter> newSims = new ArrayList<>();

        for (int day = previousDay + 1; day <= previousDay + daysPassed; day++) {
            for (SimCharacter sim : sims) {
                List<Child> newborns = sim.getFamily().onNewDay(day);
                for (Child baby : newborns) {
                    pendingNewborns.add(baby);
                    notify("A baby was born! Gender: " + baby.getGender());
                }

                List<Child> grownUps = new ArrayList<>(sim.getFamily().getGrownUpChildren());
                for (Child child : grownUps) {
                    SimCharacter newSim = new SimCharacter(child.getName(), child.getGender());
                    newSims.add(newSim);
                    sim.getFamily().removeChild(child);
                    notify(child.getName() + " has grown up and become a new Sim!");
                }
            }
        }

        sims.addAll(newSims);
        /* 
        // Daily updates
        for (int day = previousDay + 1; day <= previousDay + daysPassed; day++) {
            for (SimCharacter sim : sims) {
                sim.getFamily().onNewDay(day);
            }
        }
        for (SimCharacter sim : sims) {
            List<Child> grownUps = new ArrayList<>(sim.getFamily().getGrownUpChildren());
            for (Child child : grownUps) {
                SimCharacter newSim = new SimCharacter(child.getName(), child.getGender());
                sims.add(newSim);
                sim.getFamily().removeChild(child);
                notify(child.getName() + " has grown up and become a new Sim!");
            }
        }*/
    }

    @Override
    public int getCurrentDay() {
        return time.getCurrentDay();
    }

    @Override
    public int getCurrentHour() {
        return time.getCurrentHour();
    }

    @Override
    public void notify(String message) {
        // Engine-level message hook
        // Could later route to UI instead of printing
        System.out.println(message);
    }

    public boolean isGameOver() {
        for (SimCharacter sim : sims) {
            if (sim.isAlive()) return false;
        }
        return true;
    }
}