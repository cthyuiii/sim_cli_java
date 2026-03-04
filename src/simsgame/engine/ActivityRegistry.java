package simsgame.engine;

import java.util.ArrayList;
import java.util.List;

import simsgame.activity.AbstractActivity;
import simsgame.activity.EatActivity;
import simsgame.activity.PlayActivity;
import simsgame.activity.ShowerActivity;
import simsgame.activity.SleepActivity;
import simsgame.activity.SocialiseActivity;

public class ActivityRegistry {
    public static List<AbstractActivity> activityRegistry = new ArrayList<>();

    static {
        activityRegistry.add(new EatActivity());
        activityRegistry.add(new PlayActivity());
        activityRegistry.add(new ShowerActivity());
        activityRegistry.add(new SleepActivity());
        activityRegistry.add(new SocialiseActivity());
    }

    public List<AbstractActivity> getAllActivities() {
        return activityRegistry;
    }
}
