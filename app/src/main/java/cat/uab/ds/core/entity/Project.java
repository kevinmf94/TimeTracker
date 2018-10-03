package cat.uab.ds.core.entity;

import java.io.Serializable;
import java.util.ArrayList;

import cat.uab.ds.core.utils.ActivitiyVisitor;

/**
 * Represents project with sub-projects and tasks
 */
public class Project extends Activity implements Serializable {

    private ArrayList<Activity> activities = new ArrayList<>();

    public Project(String name) {
        super(name);
    }

    public Project(String name, String description) {
        super(name, description);
    }

    @Override
    public void aceptar(ActivitiyVisitor v) {
        if(!isRoot())
            v.visitProject(this);

        for(Activity activity : this.activities){
            activity.aceptar(v);
        }
    }

    public void addActivity(Activity activity){
        this.activities.add(activity);
    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }
}
