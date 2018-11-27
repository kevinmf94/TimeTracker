package cat.uab.ds.ui.adapters;

import java.io.Serializable;

import cat.uab.ds.core.entity.Activity;

public class ActivityHolder implements Serializable {

    private String name;
    private int duration;
    private boolean isTask;
    private boolean isProject;
    private boolean isRunning;

    public ActivityHolder(Activity activity) {
        name = activity.getName();
        duration = activity.getDuration();
        isTask = activity.isTask();
        isProject = activity.isProject();
        isRunning = activity.isRunning();
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public boolean isTask() {
        return isTask;
    }

    public boolean isProject() {
        return isProject;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
