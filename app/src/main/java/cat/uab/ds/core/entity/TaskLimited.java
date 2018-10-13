package cat.uab.ds.core.entity;

import java.io.Serializable;

/**
 * TaskLimited
 * Implementation add functionality to limit task to certain time.
 */
public class TaskLimited extends TaskDecorator implements Serializable {

    private int timeLimit;

    public TaskLimited(Task task, int timeLimit) {
        super(task);
        this.timeLimit = timeLimit;
    }

    @Override
    public void update() {
        this.getTask().update();

        if(getDuration() >= timeLimit){
            this.stop();
        }
    }
}
