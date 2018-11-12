package cat.uab.ds.core.utils;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import cat.uab.ds.core.entity.Interval;

public abstract class ReportVisitor implements ActivityVisitor {

    private static final int MINUTE = 60;

    private static final SimpleDateFormat FORMAT =
            new SimpleDateFormat("dd/MM/YYYY, HH:mm:ss",
                    new Locale("en"));

    private Date startDate = null;
    private Date endDate = null;

    /**
     * Initialize basic menu info with table header.
     * @param newStartDate Start period
     * @param newEndDate End period
     */
    public ReportVisitor(final Date newStartDate, final Date newEndDate) {
        this.startDate = newStartDate;
        this.endDate = newEndDate;
    }

    /**
     * Converts a number in milliseconds to readable duration string (Hours,
     * Minutes and Seconds).
     * @param time Duration of activity in milliseconds
     * @return Duration string
     */
    protected String durationToStr(final int time) {
        long hours = time / MINUTE / MINUTE;
        long minutes = time / MINUTE;
        long seconds = time % MINUTE;

        return String.format(new Locale("en"),
                "%dh %dm %ds", hours, minutes, seconds);
    }

    public abstract String getResult();

    public final Date getStartDate() {
        return startDate;
    }

    public final void setStartDate(final Date newStartDate) {
        this.startDate = newStartDate;
    }

    public final Date getEndDate() {
        return endDate;
    }

    public final void setEndDate(final Date newEndDate) {
        this.endDate = newEndDate;
    }

    public final String getDateFormated(final Date date) {
        return FORMAT.format(date);
    }

    public final int getDurationNormalized(
            final Date intervalStart, final Date intervalEnd) {
        Date start, end;

        if (intervalStart.compareTo(startDate) < 0) {
            start = startDate;
        } else {
            start = intervalStart;
        }

        if (intervalEnd.compareTo(endDate) >= 0) {
            end = endDate;
        } else {
            end = intervalEnd;
        }

        return Interval.getDuration(start, end);
    }

    public final ReportInterval getDurationByIntervals(
            final Collection<Interval> intervals) {

        Date start = null, end = null;
        Date tmpStart, tmpEnd;
        int duration = 0;
        for (Interval interval: intervals) {
            tmpStart = interval.getStart();
            tmpEnd = interval.getEnd();
            duration += getDurationNormalized(tmpStart, tmpEnd);

            if (start == null || tmpStart.compareTo(start) < 0) {
                start = tmpStart;
            }
            if (end == null || tmpEnd.compareTo(end) > 0) {
                end = tmpEnd;
            }

            if (start.compareTo(startDate) < 0) {
                start = startDate;
            }
            if (end.compareTo(endDate) >= 0) {
                end = endDate;
            }
        }

        return new ReportInterval(start, end, duration);
    }



    class ReportInterval {

        private Date start;
        private Date end;
        private int duration;

        ReportInterval(final Date reportStartDate,
                       final Date reportEndDate,
                       final int durationInterval) {
            this.start = reportStartDate;
            this.end = reportEndDate;
            this.duration = durationInterval;
        }

        public Date getStart() {
            return start;
        }

        public Date getEnd() {
            return end;
        }

        public int getDuration() {
            return duration;
        }
    }

}
