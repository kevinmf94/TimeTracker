package cat.uab.ds.core.entity;

/**
 * TaskScheduled. Implements scheduled functionality to task.
 */
public class TaskScheduled extends TaskDecorator {

    public TaskScheduled(Task task) {
        super(task);
    }

    @Override
    public void prueba() {
        getTask().prueba();
        System.out.println("PRUEBA3");
    }

}
