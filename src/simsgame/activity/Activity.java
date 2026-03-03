package simsgame.activity;
import java.util.List;
import java.util.ArrayList;

public class Activity {
    public static List<AbstractActivity> activityRegistry = new ArrayList<>();

    static {
        activityRegistry.add(new EatActivity());
        activityRegistry.add(new PlayActivity());
        activityRegistry.add(new ShowerActivity());
        activityRegistry.add(new SleepActivity());
        activityRegistry.add(new SocialiseActivity());
    }

}
