package cat.uab.ds.core.entity;

import java.util.ArrayList;

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

    //Temporal test
    public abstract void prueba();

    public ArrayList<Interval> getIntervals() {
        return intervals;
    }

    public void setIntervals(ArrayList<Interval> intervals) {
        this.intervals = intervals;
    }

    @Override
    public void aceptar(ActivitiyVisitor v) {
        v.visitTask(this);
    }
}
