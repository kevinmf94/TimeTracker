package cat.uab.ds.core.utils;

import java.util.Date;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import cat.uab.ds.core.entity.Configuration;

/**
 * Clock singleton for task time synchronization.
 * Is an Observable class that do thick every x miliseconds (Value set in configuration class)
 */
public class Clock extends Observable {

    //Singleton instance
    private static Clock instance;

    private Timer timer;

    /**
     * Clock constructor.
     * Init timer and schelude task to notify Observers (Time set on connfiguration)
     */
    private Clock() {
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(doTick, 0, Configuration.MIN_TIME*Configuration.SEC_TO_MILIS);
    }

    /**
     * Return instance of Clock, if this is not instatiated,
     * create new instance.
     * @return Clock instance
     */
    public static Clock getInstance() {
        if (instance == null)
            instance = new Clock();

        return instance;
    }

    /**
     * TimerTask Runnable executed every tick of Observable
     */
    private TimerTask doTick = new TimerTask() {
        @Override
        public void run() {
            instance.setChanged();
            instance.notifyObservers(new Date());
        }
    };

}
