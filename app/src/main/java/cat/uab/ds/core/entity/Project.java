package cat.uab.ds.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

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
            v.visitActivity(this);

        for(Activity activity : this.activities){
            activity.aceptar(v);
        }
    }

    @Override
    public Date getStart() {
        Date start = null;
        Date aux = null;
        for (Activity activity: this.activities) {
            aux = activity.getStart();
            if(aux == null)
                continue;

            if(start == null)
                start = aux;
            else {
                if (start.after(aux))
                    start = aux;
            }
        }

        return start;
    }

    @Override
    public Date getEnd() {
        Date end = null;
        Date aux = null;
        for (Activity activity: this.activities) {
            aux = activity.getEnd();
            if(aux == null)
                continue;

            if(end == null)
                end = aux;
            else
                if (end.before(aux))
                    end = aux;
        }

        return end;
    }

    @Override
    public int getDuration() {
        int duration = 0;

        for (Activity activity: this.activities) {
            duration += activity.getDuration();
        }

        return duration;
    }

    public void addActivity(Activity activity){
        activity.setLevel(this.getLevel()+1);
        this.activities.add(activity);
    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }
}
