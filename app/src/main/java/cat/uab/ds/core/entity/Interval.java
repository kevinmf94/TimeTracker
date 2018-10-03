package cat.uab.ds.core.entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

/**
 * Represents interval of time
 */
public class Interval implements Observer, Serializable {

    private Date start;
    private Date end;

    public long getDuration(){
        if(end != null){
            return end.getTime() - start.getTime();//as given
        }else{
            return  0;
        }
    }

    public String getDurationStr(){
        long diff = getDuration();

        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        long hours = TimeUnit.MILLISECONDS.toMinutes(diff);
        long days = TimeUnit.MILLISECONDS.toMinutes(diff);

        return ((days!=0)?"Days: "+ days:"")
                + ((hours!=0)?"Hours: "+ hours:"")
                + ((minutes!=0)?"Minutes: "+ minutes:"")
                + ((seconds!=0)?"Seconds: "+ seconds:"");
    }

    @Override
    public String toString() {

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        String endStr = " - ";
        String durationStr = "";

        if(end != null){
            endStr = df.format(end);
            durationStr = " | " + getDurationStr();
        }

        return "Start: " + df.format(start) + " End: "+ endStr + durationStr;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof Date)
            this.end = (Date) arg;
    }
}
