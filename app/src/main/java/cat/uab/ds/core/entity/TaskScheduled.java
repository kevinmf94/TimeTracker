package cat.uab.ds.core.entity;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import cat.uab.ds.core.utils.Clock;

/**
 * TaskScheduled. Implements scheduled functionality to task.
 */
public class TaskScheduled extends TaskDecorator implements Observer {

    private final Date dateToStart;

    /**
     * TaskScheduled Constructor.
     * Register this instance to Clock Observable.
     * @param task Task or TaskDecorator to wrap.
     * @param newDateToStart Date to start task.
     */
    public TaskScheduled(final Task task, final Date newDateToStart) {
        super(task);
        Clock.getInstance().addObserver(this);
        this.dateToStart = newDateToStart;
    }

    /**
     * Receive notify of Clock observable and check if the dateToStart is after
     * and run the task.
     * @param o Observable Clock
     * @param arg Date
     */
    @Override
    public void update(final Observable o, final Object arg) {
        if (arg instanceof Date) {
            Date now = (Date) arg;
            if (now.after(dateToStart)) {
                super.start();
                Clock.getInstance().deleteObserver(this);
            }
        }
    }
}
