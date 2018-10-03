package cat.uab.ds.core.entity;

import java.io.Serializable;

/**
 * TaskScheduled. Implements scheduled functionality to task.
 */
public class TaskScheduled extends TaskDecorator implements Serializable {

    public TaskScheduled(Task task) {
        super(task);
    }

    @Override
    public void prueba() {
        getTask().prueba();
        System.out.println("PRUEBA3");
    }

}
