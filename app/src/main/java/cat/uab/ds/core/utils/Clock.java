package cat.uab.ds.core.utils;

import java.util.Date;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import cat.uab.ds.core.entity.Configuration;

/**
 * Clock singleton for task time synchronization.
 * Is an Observable class that do thick every x milliseconds (Value set in
 * configuration class)
 */
public final class Clock extends Observable {

    //Singleton instance
    private static Clock instance = null;

    private final Timer timer;

    /**
     * Clock constructor.
     * Init timer and schedule task to notify Observers (Time set on
     * configuration)
     */
    private Clock() {
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                instance.setChanged();
                instance.notifyObservers(new Date());
            }
        }, 0, Configuration.MIN_TIME * Configuration.SECONDS_TO_MILLISECONDS);
    }

    /**
     * Return instance of Clock, if this is not instantiated,
     * create new instance.
     * @return Clock instance
     */
    public static Clock getInstance() {
        if (instance == null) {
            instance = new Clock();
        }
        return instance;
    }

    public void stopTimer() {
        this.timer.cancel();
    }

}
