package cat.uab.ds.core.entity;

/**
 * Task implements basic functionality.
 */
public class TaskBasic extends Task {
    /**
     * Task constructor.
     * @param name The name of the task
     */
    public TaskBasic(final String name) {
        super(name);
    }
    /**
     * Task constructor.
     * @param name The name of the task
     * @param description The description of the task
     */
    public TaskBasic(final String name, final String description) {
        super(name, description);
    }

}
