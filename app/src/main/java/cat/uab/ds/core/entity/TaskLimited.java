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
    }

    @Override
    public void update() {
        System.out.println("Limited");

        if(getDuration() >= timeLimit){
            super.stop();
            Clock.getInstance().deleteObserver(this);
        }

        super.update();
    }

    @Override
    public void start() {
        Clock.getInstance().addObserver(this);
        super.start();
    }

    @Override
    public void update(Observable o, Object arg) {
        this.update();
    }
}
