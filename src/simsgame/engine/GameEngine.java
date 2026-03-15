package simsgame.engine;

import java.util.ArrayList;
import java.util.List;

import simsgame.sims.SimCharacter;
import simsgame.activity.Activity;
import simsgame.career.Career;
import simsgame.career.CareerFactory;

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

    private Time time;
    private ActivityRegistry activityRegistry;

    public GameEngine() {
        sims = new ArrayList<>();
        activeSimIndex = -1;
        time = new Time();
        activityRegistry = new ActivityRegistry();
    }

    // =====================================================
    // Game Lifecycle
    // =====================================================

    public void startNewGame() {
        sims.clear();
        activeSimIndex = -1;
        time.reset();
    }

    // =====================================================
    // Sim Management
    // =====================================================

    public void createSim(String name) {
        SimCharacter sim = new SimCharacter(name);
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

    private SimCharacter getActiveSim() {
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
        return CareerFactory.getCareerNames();
    }

    public String assignCareerToActiveSim(int optionIndex) {
        SimCharacter sim = getActiveSim();
        if (sim == null) return "No active Sim.";

        Career career = CareerFactory.createCareer(optionIndex);
        if (career == null) return "Invalid career option.";

        sim.setCareer(career);
        return sim.getName() + " is now a " + career.getCareerName();
    }

    // =====================================================
    // Family (Optional)
    // =====================================================

    public String tryForBabyActiveSim() {
        SimCharacter sim = getActiveSim();
        if (sim == null) return "No active Sim.";

        if (sim.getWallet().getBalance() < 100)  { return "You have insufficient funds to raise a baby!"; }
        
        if (sim.getNeeds().getSocial() < 50 || sim.getNeeds().getEnergy() < 40) { return "You do not feel like trying for a child at the moment.."; }

        return sim.getFamily().tryForBaby(sim.getName(), time.getCurrentDay());
    }

    // =====================================================
    // Time Handling
    // =====================================================

    @Override
    public void advanceTime(int hours) {
        int daysPassed = time.advance(hours);
        int previousDay = time.getCurrentDay();

        // Hourly updates
        for (SimCharacter sim : sims) {
            sim.getNeeds().decay(hours);
            sim.getMood().update(sim.getNeeds());
        }

        // Daily updates
        for (int day = previousDay + 1; day <= previousDay + daysPassed; day++) {
            for (SimCharacter sim : sims) {
                sim.getFamily().onNewDay(day);
            }
        }
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
}