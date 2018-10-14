package cat.uab.ds.core.entity;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

import cat.uab.ds.core.utils.Clock;

/**
 * TaskLimited
 * Implementation add functionality to limit task to certain time.
 */
public class TaskLimited extends TaskDecorator implements Serializable, Observer {

    private int timeLimit;

    public TaskLimited(Task task, int timeLimit) {
        super(task);
        this.timeLimit = timeLimit;
        Clock.getInstance().addObserver(this);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(isRunning()) {
            if (getDuration() >= timeLimit) {
                super.stop();
            }
        }
    }
}
