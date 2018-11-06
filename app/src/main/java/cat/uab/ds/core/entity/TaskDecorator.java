package cat.uab.ds.core.entity;

import java.util.Date;

/**
 * TaskDecorator implements decorator pattern for Task
 */
public abstract class TaskDecorator extends Task {

    private Task task;
    private boolean firstDecorator = false;

    /**
     * Task Decorator, additional feature to Task.
     * @param task Task to wrap.
     */
    TaskDecorator(Task task) {
        if(!(task instanceof  TaskDecorator))
            this.firstDecorator = true;

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
    void start() {
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

    public boolean isFirstDecorator() {
        return firstDecorator;
    }

    public void setFirstDecorator(boolean firstDecorator) {
        this.firstDecorator = firstDecorator;
    }
}


