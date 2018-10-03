package cat.uab.ds.core.entity;

import java.util.ArrayList;

import cat.uab.ds.core.utils.ActivitiyVisitor;

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

    @Override
    public void aceptar(ActivitiyVisitor v) {
        v.visitProject(this);
    }

    public void addActivity(Activity activity){
        this.activities.add(activity);
    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }
}
