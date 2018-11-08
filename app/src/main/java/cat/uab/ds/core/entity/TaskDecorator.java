package cat.uab.ds.core.entity;

import java.util.Date;

/**
 * TaskDecorator implements decorator pattern for Task.
 */
public abstract class TaskDecorator extends Task {

    private Task task;
    private boolean firstDecorator = false;

    /**
     * Task Decorator, additional feature to Task.
     * @param newTask Task to wrap.
     */
    TaskDecorator(final Task newTask) {
        if (!(newTask instanceof  TaskDecorator)) {
            this.firstDecorator = true;
        }

        this.task = newTask;
    }

    public final Task getTask() {
        return task;
    }

    public final void setTask(final Task newTask) {
        this.task = newTask;
    }

    public final Task removeLastComponent() {
        return task;
    }

    @Override
    public final void start() {
        task.start();
    }

    @Override
    public final void stop() {
        task.stop();
    }

    @Override
    public final Date getStart() {
        return task.getStart();
    }

    @Override
    public final Date getEnd() {
        return task.getEnd();
    }

    @Override
    public final int getDuration() {
        return task.getDuration();
    }

    @Override
    public final String getName() {
        return task.getName();
    }

    @Override
    public final String getDescription() {
        return task.getDescription();
    }

    @Override
    public final boolean isRunning() {
        return task.isRunning();
    }

    public final boolean isFirstDecorator() {
        return firstDecorator;
    }

    public final void setFirstDecorator(final boolean newFirstDecorator) {
        this.firstDecorator = newFirstDecorator;
    }
}


