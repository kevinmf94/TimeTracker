package cat.uab.ds.core.entity;

import java.io.Serializable;

/**
 * TaskLimited
 * Implementation add functionality to limit task to certain time.
 */
public class TaskLimited extends TaskDecorator implements Serializable {

    public TaskLimited(Task task) {
        super(task);
    }

    @Override
    public void operaciondecorator() {
        getTask().operaciondecorator();
        System.out.println("PRUEBA2");
    }
}
