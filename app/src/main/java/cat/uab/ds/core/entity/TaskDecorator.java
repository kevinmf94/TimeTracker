package cat.uab.ds.core.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * TaskDecorator implements decorator pattern for Task
 */
public abstract class TaskDecorator extends Task implements Serializable {

    private Task task;
    private boolean firstDecorator = false;

    /**
     * Task Decorator, additional feature to Task.
     * @param task Task to wrap.
     */
    public TaskDecorator(Task task) {
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

    public Task removeComponent(Class <? extends TaskDecorator> className){

        TaskDecorator next = null;
        TaskDecorator actual = this;

        if(className.getName() == this.getClass().getName() && next == null)
            return getTask();
        else {

        }

        /*System.out.println(className.getName());
        System.out.println(this.getClass().getName());
        System.out.println(this.getTask().getClass().getName());
        System.out.println(this.getTask().getClass().getName() == className.getName());*/
        /*if(this.getClass(). instanceof  className.){

        }*/
        return null;
    }

    public boolean isFirstDecorator() {
        return firstDecorator;
    }

    public void setFirstDecorator(boolean firstDecorator) {
        this.firstDecorator = firstDecorator;
    }
}


