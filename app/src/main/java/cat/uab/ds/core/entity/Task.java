package cat.uab.ds.core.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;

import cat.uab.ds.core.utils.ActivityVisitor;

/**
 * Task abstract.
 */
public abstract class Task extends Activity {

    private final Logger logger = LoggerFactory.getLogger(Task.class);
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
        this.invariant();
        logger.info("Create task " + name);
    }

    /**
     * Task constructor from other tasks.
     * @param task Task instance
     */
    public Task(final Task task) {
        assert task != null;
        this.setName(task.getName());
        this.setDescription(task.getDescription());
        this.setIntervals(task.getIntervals());
        this.invariant();
    }

    /**
     * List of intervals for this task.
     * @return Intervals list
     */
    public ArrayList<Interval> getIntervals() {
        return intervals;
    }

    public final void setIntervals(final ArrayList<Interval> newIntervals) {
        this.intervals = newIntervals;
    }

    /**
     * Accept the visitor.
     * @param v ActivityVisitor instance
     */
    @Override
    public void accept(final ActivityVisitor v) {
        assert v != null;
        v.visit(this);
    }

    /**
     * Start task. Creates new interval, start it add it to task.
     */
    public void start() {
        assert this.invariant();
        if (!isRunning()) {
            logger.info("Start Task " + getName());
            Interval interval = new Interval();
            interval.start();
            intervals.add(interval);
        }
        assert isRunning();
    }

    /**
     * Stop the task. Stops the last interval inside,
     * and checks if is your duration is lower than
     * them minimumTime to save it or not. If is lower, discards the interval
     * (remove it from intervals list).
     */
    public void stop() {
        assert this.invariant();
        assert intervals != null && intervals.size() > 0;

        if (isRunning()) {
            logger.info("Stop Task " + getName());
            Interval interval = intervals.get(intervals.size() - 1);
            interval.stop();

            //If the interval duration is less than MIN_Time remove it.
            if (interval.getDuration() < Configuration.getMinimumTime()) {
                this.intervals.remove(interval);
            }
        }

        assert !isRunning();
    }

    /**
     * Gets the start Date from task, the same as start from the first interval.
     * @return Start Date
     */
    @Override
    public Date getStart() {
        assert intervals != null;

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
        assert intervals != null;

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
        assert intervals != null;

        float total = 0;
        for (Interval interval: intervals) {
            total += interval.getDuration();
        }

        assert total >= 0;
        return (int) total;
    }

    /**
     * Checks if the tasks is running. (The last interval is running)
     * @return Boolean true if is running
     */
    public boolean isRunning() {
        assert intervals != null;

        int size = this.intervals.size();
        return size != 0 && this.intervals.get(size - 1).isRunning();
    }

    private boolean invariant() {
        if(getStart() != null && getEnd() != null) {
            if (!(getStart().before(getEnd()))) {
                return false;
            }
        }
        if (!(getDuration() >= 0)) {
            return false;
        }
        return true;
    }
}
