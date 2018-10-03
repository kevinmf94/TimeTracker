package cat.uab.ds.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cat.uab.ds.core.entity.Activity;
import cat.uab.ds.core.entity.Configuration;
import cat.uab.ds.core.entity.Project;

public class TimeTracker {

    private ArrayList<Activity> activities = new ArrayList<>();
    private Timer timer;

    public TimeTracker() {}

    public void addProject(Project project){
        this.activities.add(project);
    }

    public void startTimeTracker() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
            }
        }, 0, Configuration.MIN_TIME);
    }
}
