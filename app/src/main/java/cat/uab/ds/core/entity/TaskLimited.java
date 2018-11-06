package cat.uab.ds.core.entity;

import java.util.Observable;
import java.util.Observer;

import cat.uab.ds.core.utils.Clock;

/**
 * TaskLimited
 * Implementation add functionality to limit task to certain time.
 */
public class TaskLimited extends TaskDecorator implements Observer {

    private final int timeLimit;

    /**
     * Additional feature to Task, limit time of duration
     * @param task Task or TaskDecorator to wrap.
     * @param timeLimit Time in seconds
     */
    public TaskLimited(Task task, int timeLimit) {
        super(task);
        this.timeLimit = timeLimit;
        Clock.getInstance().addObserver(this);
    }

    /**
     * Receive notify of Clock observable and check if getDuration is greater then
     * timeLimit stops this task.
     * @param o Observable Clock
     * @param arg Date
     */
    @Override
    public void update(Observable o, Object arg) {
        if(isRunning()) {
            if (getDuration() >= timeLimit) {
                super.stop();
            }
        }
    }
}
