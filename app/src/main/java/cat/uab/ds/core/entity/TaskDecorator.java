package cat.uab.ds.core.entity;

import java.io.Serializable;

/**
 * TaskDecorator implements decorator pattern for Task
 */
public abstract class TaskDecorator extends Task implements Serializable {

    private Task task;

    public TaskDecorator(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Task removeLastComponent(){
        return task;
    }
}


