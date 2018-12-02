package cat.uab.ds.ui.adapters;

import java.io.Serializable;

import cat.uab.ds.core.entity.Interval;
import cat.uab.ds.core.utils.DateUtils;

public class IntervalHolder implements Serializable {

    private String dateStart;
    private String dateEnd;
    private boolean isRunning;
    private int duration;

    public IntervalHolder(Interval interval) {
        this.isRunning = interval.isRunning();
        this.duration = (int) interval.getDuration();
        this.dateStart = DateUtils.dateToStr(interval.getStart());
        this.dateEnd = DateUtils.dateToStr(interval.getEnd());
    }

    public String getDateStart() {
        return dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getDuration() {
        return duration;
    }
}
