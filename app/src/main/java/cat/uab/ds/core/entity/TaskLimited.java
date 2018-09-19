package cat.uab.ds.core.entity;

/**
 * TaskLimited
 * Implementation add functionality to limit task to certain time.
 */
public class TaskLimited extends TaskDecorator {

    public TaskLimited(Task task) {
        super(task);
    }

    @Override
    public void prueba() {
        getTask().prueba();
        System.out.println("PRUEBA2");
    }
}
