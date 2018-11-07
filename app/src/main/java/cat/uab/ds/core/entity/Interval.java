package cat.uab.ds.core.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import cat.uab.ds.core.utils.Clock;

/**
 * Represents interval of time.
 */
class Interval implements Observer, Serializable {

    private Date start = null;
    private Date end = null;

    private boolean isRunning = false;

    /**
     * Interval Constructor. Register yourself to Clock Observable updates.
     */
    Interval() {
        Clock.getInstance().addObserver(this);
    }

    /**
     * Get the duration by the difference between start and end Dates.
     * @return Total duration in milliseconds
     */
    public long getDuration() {
        if (end != null) {
            return end.getTime() - start.getTime();
        } else {
            return  0;
        }
    }

    /**
     * When is notified by Observable, updates the end Date.
     * @param o Observable instance
     * @param arg Date notified by the Observable (Clock in this case)
     */
    @Override
    public void update(final Observable o, final Object arg) {
        if (arg instanceof Date) {
            this.end = (Date) arg;
        }
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    /**
     * Set the start Date to NOW, and change state to isRunning.
     */
    public void start() {
        start = new Date();
        setRunning(true);
    }

    /**
     * Changes state to not running and unregister yourself
     * from Clock observers list.
     */
    public void stop() {
        setRunning(false);
        Clock.getInstance().deleteObserver(this);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(final boolean running) {
        isRunning = running;
    }
}
