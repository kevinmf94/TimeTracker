package cat.uab.ds.core.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;

import cat.uab.ds.core.utils.ActivitiyVisitor;

/**
 * Activity, represents conjunt of projects and tasks
 */
public abstract class Activity implements Serializable {

    private int level = 0;
    private String name;
    private String description;

    private boolean isRoot = false;//For dummy root project container

    protected Activity(){};

    public Activity(String name) {
        this(name, "");
    }

    /**
     * Activity Contructor
     * @param name Name of Activity (Task or Project)
     * @param description Description of Activity
     */
    public Activity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Used for Visitor Pattern to generate view
     * @param v ActivitiyVisitor instance
     */
    abstract public void aceptar(ActivitiyVisitor v);

    abstract public Date getStart();

    abstract public Date getEnd();

    abstract public int getDuration();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean isRoot){
        this.isRoot = isRoot;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
