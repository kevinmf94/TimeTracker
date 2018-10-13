package cat.uab.ds.core.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * TaskScheduled. Implements scheduled functionality to task.
 */
public class TaskScheduled extends TaskDecorator implements Serializable {

    private Timer timer;
    private Date dateToStart;

    public TaskScheduled(Task task, Date dateToStart) {
        super(task);
        this.dateToStart = dateToStart;
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getTask().start();
            }
        }, this.dateToStart);
    }

    @Override
    public void update() {
        getTask().update();
    }

}
