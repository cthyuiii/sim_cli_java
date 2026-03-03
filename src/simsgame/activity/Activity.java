package simsgame.activity;
import java.util.List;
import java.util.ArrayList;

public class Activity {
    public static List<AbstractActivity> activities = new ArrayList<>();

    static {
        activities.add(new EatActivity());
        activities.add(new SleepActivity());
        activities.add(new SleepActivity());
        activities.add(new SleepActivity());
    }

}
