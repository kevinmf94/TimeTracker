package cat.uab.ds.core.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import cat.uab.ds.core.utils.Clock;

/**
 * TaskScheduled. Implements scheduled functionality to task.
 */
public class TaskScheduled extends TaskDecorator implements Serializable, Observer {

    private Date dateToStart;

    public TaskScheduled(Task task, Date dateToStart) {
        super(task);
        Clock.getInstance().addObserver(this);
        this.dateToStart = dateToStart;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof Date){
            Date now = (Date) arg;
            if(now.after(dateToStart)) {
                super.start();
                Clock.getInstance().deleteObserver(this);
            }
        }
    }
}
