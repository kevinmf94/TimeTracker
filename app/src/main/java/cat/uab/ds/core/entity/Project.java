package cat.uab.ds.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import cat.uab.ds.core.utils.ActivitiyVisitor;

/**
 * Represents project with sub-projects and tasks.
 */
public class Project extends Activity implements Serializable {

    private final Collection<Activity> activities = new ArrayList<>();

    /**
     * Project constructor.
     * @param name The name of the project
     */
    public Project(String name) {
        super(name);
    }

    /**
     * Project constructor.
     * @param name The name of the project
     * @param description The description of the project
     */
    public Project(String name, String description) {
        super(name, description);
    }

    /**
     * Used for Visitor Pattern to generate view.
     * Visits children activities and if not is the root project, visit yourself.
     * @param v ActivitiyVisitor instance
     */
    @Override
    public void accept(ActivitiyVisitor v) {
        if(!isRoot())
            v.visitActivity(this);

        for(Activity activity : this.activities){
            activity.accept(v);
        }
    }

    /**
     * Gets the start Date of the project with the activity that starts early.
     * @return Date with start
     */
    @Override
    public Date getStart() {
        Date start = null;
        Date aux;
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

    /**
     * Gets the end Date of the project with the activity that ends late.
     * @return Date with end
     */
    @Override
    public Date getEnd() {
        Date end = null;
        Date aux;
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

    /**
     * Gets the duration of the project adding the children activities duration (Projects or Tasks)
     * @return Duration in milliseconds
     */
    @Override
    public int getDuration() {
        int duration = 0;

        for (Activity activity: this.activities) {
            duration += activity.getDuration();
        }

        return duration;
    }

    /**
     * Adds activity to the project (Another project or task)
     * @param activity Activity to add
     */
    public void addActivity(Activity activity){
        activity.setLevel(this.getLevel()+1);
        this.activities.add(activity);
    }

    /**
     * Gets the children activities list of the project.
     * @return Activities list
     */
    public Collection<Activity> getActivities() {
        return activities;
    }
}
