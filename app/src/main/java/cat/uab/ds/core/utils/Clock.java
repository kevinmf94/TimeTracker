package cat.uab.ds.core.utils;

import java.util.Date;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import cat.uab.ds.core.entity.Configuration;

public class Clock extends Observable {

    //Singleton
    private static Clock instance;

    private Timer timer;

    private Clock() {
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(doTick, 0, Configuration.MIN_TIME);
    }

    public static Clock getInstance(){
        if(instance == null)
            instance = new Clock();

        return instance;
    }

    private TimerTask doTick = new TimerTask() {
        @Override
        public void run() {
            instance.setChanged();
            instance.notifyObservers(new Date());
        }
    };

}
