package simsgame.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import simsgame.activity.*;

/**
 * ActivityRegistry owns the list of available activities.
 * GameEngine uses this to display menus and resolve an activity by index.
 */
public class ActivityRegistry {

    private final List<Activity> activities;

    public ActivityRegistry() {
        List<Activity> temp = new ArrayList<>();

        // Tune these numbers to your game balance
        temp.add(new EatActivity(10, 25, 1));         // cost $10, +25 hunger, 1 hour
        temp.add(new SleepActivity(40, 8));           // +40 energy, 8 hours
        temp.add(new ShowerActivity(30, 1));          // +30 hygiene, 1 hour
        temp.add(new SocialiseActivity(25, 10, 2));   // +25 social, +10 fun, 2 hours
        temp.add(new PlayActivity(25, 10, 2, 5));     // +25 fun, -10 energy, 2 hours, cost $5
        temp.add(new WorkActivity());                 // hours determined by Career (or fallback)

        activities = Collections.unmodifiableList(temp);
    }

    public List<Activity> getAllActivities() {
        return activities;
    }

    public Activity getActivityByIndex(int index) {
        if (index < 0 || index >= activities.size()) return null;
        return activities.get(index);
    }
}