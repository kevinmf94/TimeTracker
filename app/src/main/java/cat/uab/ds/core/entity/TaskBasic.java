package cat.uab.ds.core.entity;

import java.io.Serializable;

/**
 * Task implements basic functionality
 */
public class TaskBasic extends Task implements Serializable {

    public TaskBasic(String name) {
        super(name);
    }

    public TaskBasic(String name, String description) {
        super(name, description);
    }

}
