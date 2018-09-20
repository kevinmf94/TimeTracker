package cat.uab.ds.core.entity;

import java.util.ArrayList;

/**
 * Represents project with sub-projects and tasks
 */
public class Project extends Activity {

    private ArrayList<Activity> activities = new ArrayList<>();

    public Project(String name) {
        super(name);
    }

    public Project(String name, String description) {
        super(name, description);
    }

    public void addActivity(Activity activity){
        this.activities.add(activity);
    }
}
