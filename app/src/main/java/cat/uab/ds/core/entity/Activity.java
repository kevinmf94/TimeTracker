package cat.uab.ds.core.entity;

import java.io.Serializable;
import java.util.Date;

import cat.uab.ds.core.utils.ActivityVisitor;

/**
 * Activity, represents a set of projects and tasks.
 */
public abstract class Activity implements Serializable {

    private int level = 0;
    private String name;
    private String description;

    private boolean isRoot = false; //For dummy root project container

    Activity() { }

    /**
     * Activity Constructor.
     * @param newName Name of activity
     */
    Activity(final String newName) {
        this(newName, "");
    }

    /**
     * Activity Constructor.
     * @param newName Name of Activity (Task or Project)
     * @param newDescription Description of Activity
     */
    protected Activity(final String newName, final String newDescription) {
        this.name = newName;
        this.description = newDescription;
    }

    public String getName() {
        return name;
    }

    protected void setName(final String newName) {
        this.name = newName;
    }

    protected String getDescription() {
        return description;
    }

    protected final void setDescription(final String newDescription) {
        this.description = newDescription;
    }

    public final boolean isRoot() {
        return isRoot;
    }

    public final void setRoot(final boolean newIsRoot) {
        this.isRoot = newIsRoot;
    }

    public final int getLevel() {
        return level;
    }

    protected void setLevel(final int newLevel) {
        this.level = newLevel;
    }

    /**
     * Used for Visitor Pattern to generate view.
     * @param v ActivityVisitor instance
     */
    public abstract void accept(ActivityVisitor v);

    public abstract Date getStart();

    public abstract Date getEnd();

    public abstract int getDuration();
}
