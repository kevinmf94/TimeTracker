package cat.uab.ds.core.entity;

import java.util.ArrayList;
import java.util.Date;

import cat.uab.ds.core.utils.ActivitiyVisitor;

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

    public Task(Task task){
        this.setName(task.getName());
        this.setDescription(task.getDescription());
    }

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
        if(!isRunning()) {
            Interval interval = new Interval();
            interval.start();
            intervals.add(interval);
        }
    }

    public void stop() {
        if(isRunning()) {
            Interval interval = intervals.get(intervals.size() - 1);
            interval.stop();

            //If the interval duration is less than MIN_Time remove it.
            if(interval.getDuration() < Configuration.MIN_TIME)
                this.intervals.remove(interval);
        }
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
        float total = 0;
        for (Interval interval: intervals) {
            total += interval.getDuration();
        }

        return Math.round(total/Configuration.SEC_TO_MILIS);
    }

    public boolean isRunning(){
        int size = this.intervals.size();
        if(size == 0)
            return false;

        return this.intervals.get(size-1).isRunning();
    }
}
