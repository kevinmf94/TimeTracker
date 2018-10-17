package cat.uab.ds.core.entity;

import java.io.Serializable;

/**
 * Task implements basic functionality
 */
public class TaskBasic extends Task implements Serializable {
    /**
     * Task constructor.
     * @param name The name of the task
     */
    public TaskBasic(String name) {
        super(name);
    }
    /**
     * Task constructor.
     * @param name The name of the task
     * @param description The description of the task
     */
    public TaskBasic(String name, String description) {
        super(name, description);
    }

}
