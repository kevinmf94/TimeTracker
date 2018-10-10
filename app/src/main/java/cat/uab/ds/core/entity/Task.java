package cat.uab.ds.core.entity;

import java.util.ArrayList;
import java.util.Date;

import cat.uab.ds.core.utils.ActivitiyVisitor;
import cat.uab.ds.core.utils.Clock;

/**
 * Task abstract
 */
public abstract class Task extends Activity {

    private ArrayList<Interval> intervals = new ArrayList<>();

    protected Task(){};

    public Task(String name) {
        super(name);
    }

    public Task(String name, String description) {
        super(name, description);
    }

    //Temporal test decorator
    //TODO Rename
    public abstract void operaciondecorator();

    public ArrayList<Interval> getIntervals() {
        return intervals;
    }

    public void setIntervals(ArrayList<Interval> intervals) {
        this.intervals = intervals;
    }

    @Override
    public void aceptar(ActivitiyVisitor v) {
        v.visitActivity(this);
    }

    public void start() {
        Interval interval = new Interval();
        interval.start();
        intervals.add(interval);
    }

    public void stop() {
        Interval interval = intervals.get(intervals.size()-1);
        interval.stop();
    }

    @Override
    public Date getStart() {
        if(intervals.size() == 0)
            return null;

        return intervals.get(0).getStart();
    }

    @Override
    public Date getEnd() {
        if(intervals.size() == 0)
            return null;

        return intervals.get(intervals.size()-1).getEnd();
    }

    /**
     * Calculate duration of task
     * @return int Duration sum of intervals
     */
    public int getDuration() {
        int total = 0;
        for (Interval interval: intervals) {
            total += interval.getDuration();
        }

        return total;
    }
}
