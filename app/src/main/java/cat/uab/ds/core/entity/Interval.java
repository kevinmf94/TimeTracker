package cat.uab.ds.core.entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import cat.uab.ds.core.utils.Clock;

/**
 * Represents interval of time
 */
public class Interval implements Observer, Serializable {

    private Date start = null;
    private Date end = null;

    private boolean isRunning = false;

    public Interval() {
        Clock.getInstance().addObserver(this);
    }

    public long getDuration(){
        if(end != null){
            return end.getTime() - start.getTime();//as given
        }else{
            return  0;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Date) {
            if (this.start == null)
                this.start = (Date) arg;

            this.end = (Date) arg;
        }
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public void start(){
        isRunning = true;
    }

    public void stop() {
        end = new Date();
        Clock.getInstance().deleteObserver(this);
    }
}
