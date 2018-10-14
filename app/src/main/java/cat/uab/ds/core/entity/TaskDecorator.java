package cat.uab.ds.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import cat.uab.ds.core.utils.ActivitiyVisitor;

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

    @Override
    public void start() {
        task.start();
    }

    @Override
    public void stop() {
        task.stop();
    }

    @Override
    public Date getStart() {
        return task.getStart();
    }

    @Override
    public Date getEnd() {
        return task.getEnd();
    }

    @Override
    public int getDuration() {
        return task.getDuration();
    }

    @Override
    public String getName() {
        return task.getName();
    }

    @Override
    public String getDescription() {
        return task.getDescription();
    }

    @Override
    public boolean isRunning() {
        return task.isRunning();
    }
}


