package cat.uab.ds.core.entity;

/**
 * Activity, represents conjunt of projects and tasks
 */
public abstract class Activity {

    private String name;
    private String description;

    protected Activity(){};

    public Activity(String name) {
        this(name, "");
    }

    public Activity(String name, String description) {
        this.name = name;
        this.description = description;
    }

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
}
