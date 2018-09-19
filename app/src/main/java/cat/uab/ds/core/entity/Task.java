package cat.uab.ds.core.entity;

import java.util.ArrayList;

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

    //Temporal test
    public abstract void prueba();

    public ArrayList<Interval> getIntervals() {
        return intervals;
    }

    public void setIntervals(ArrayList<Interval> intervals) {
        this.intervals = intervals;
    }
}
