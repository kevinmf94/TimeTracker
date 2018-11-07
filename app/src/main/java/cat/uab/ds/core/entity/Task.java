package cat.uab.ds.core.entity;

import java.util.ArrayList;
import java.util.Date;

import cat.uab.ds.core.utils.ActivityVisitor;

/**
 * Task abstract.
 */
public abstract class Task extends Activity {

    private ArrayList<Interval> intervals = new ArrayList<>();

    protected Task() { }

    /**
     * Task constructor.
     * @param name The name of the task
     */
    public Task(final String name) {
        super(name);
    }

    /**
     * Task constructor.
     * @param name The name of the task
     * @param description The description of the task
     */
    public Task(final String name, final String description) {
        super(name, description);
    }

    /**
     * Task constructor from other tasks.
     * @param task Task instance
     */
    public Task(final Task task) {
        this.setName(task.getName());
        this.setDescription(task.getDescription());
        this.setIntervals(task.getIntervals());
    }

    /**
     * List of intervals for this task.
     * @return Intervals list
     */
    public ArrayList<Interval> getIntervals() {
        return intervals;
    }

    public void setIntervals(final ArrayList<Interval> newIntervals) {
        this.intervals = newIntervals;
    }

    /**
     * Accept the visitor.
     * @param v ActivityVisitor instance
     */
    @Override
    public void accept(final ActivityVisitor v) {
        v.visitActivity(this);
    }

    /**
     * Start task. Creates new interval, start it add it to task.
     */
    void start() {
        if (!isRunning()) {
            Interval interval = new Interval();
            interval.start();
            intervals.add(interval);
        }
    }

    /**
     * Stop the task. Stops the last interval inside,
     * and checks if is your duration is lower than
     * them MIN_TIME to save it or not. If is lower, discards the interval
     * (remove it from intervals list).
     */
    public void stop() {
        if (isRunning()) {
            Interval interval = intervals.get(intervals.size() - 1);
            interval.stop();

            //If the interval duration is less than MIN_Time remove it.
            if (interval.getDuration() < Configuration.MIN_TIME) {
                this.intervals.remove(interval);
            }
        }
    }

    /**
     * Gets the start Date from task, the same as start from the first interval.
     * @return Start Date
     */
    @Override
    public Date getStart() {
        if (intervals.size() == 0) {
            return null;
        }

        return intervals.get(0).getStart();
    }

    /**
     * Gets the end Date from task, the same as end from the first interval.
     * @return End Date
     */
    @Override
    public Date getEnd() {
        if (intervals.size() == 0) {
            return null;
        }

        return intervals.get(intervals.size() - 1).getEnd();
    }

    /**
     * Calculate duration of task.
     * @return int Duration sum of intervals
     */
    public int getDuration() {
        float total = 0;
        for (Interval interval: intervals) {
            total += interval.getDuration();
        }

        return Math.round(total / Configuration.SECONDS_TO_MILLISECONDS);
    }

    /**
     * Checks if the tasks is running. (The last interval is running)
     * @return Boolean true if is running
     */
    public boolean isRunning() {
        int size = this.intervals.size();
        if (size == 0) {
            return false;
        }

        return this.intervals.get(size - 1).isRunning();
    }
}
