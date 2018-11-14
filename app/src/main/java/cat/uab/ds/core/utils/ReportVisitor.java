package cat.uab.ds.core.utils;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import cat.uab.ds.core.entity.Interval;

public abstract class ReportVisitor implements ActivityVisitor {

    static final int MINUTE = 60;

    public static final String SEPARATOR = "|";
    static final String WHITE_LINE = "                                     "
            + "                                           ";

    // Colums positions of the projects table
    static final int POS_PROJECT_NAME = 0;
    static final int POS_PROJECT_START = 12;
    static final int POS_PROJECT_END = 35;
    static final int POS_PROJECT_DURATION = 60;

    // Date Time Format
    private static final SimpleDateFormat FORMAT =
            new SimpleDateFormat("dd/MM/YYYY, HH:mm:ss",
                    new Locale("en"));

    private Date startDate;
    private Date endDate;

    private ReportFormat reportFormat;

    /**
     * Initialize basic menu info with table header.
     * @param newStartDate Start period
     * @param newEndDate End period
     * @param newReportFormat Report format
     */
    public ReportVisitor(final Date newStartDate, final Date newEndDate,
                         final ReportFormat newReportFormat) {
        this.startDate = newStartDate;
        this.endDate = newEndDate;
        this.reportFormat = newReportFormat;
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

    public final String getDateString(final Date date) {
        return FORMAT.format(date);
    }

    /**
     * Limit the start date and end date by the limits of established range.
     * @param intervalStart The start of interval
     * @param intervalEnd The end of interval
     * @return Duration
     */
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

    /**
     * Get the total duration of intervals between start and end date.
     * @param intervals
     * @return
     */
    public final ReportInterval getDurationByIntervals(
            final Collection<Interval> intervals) {

        Date start = null, end = null;
        Date tmpStart, tmpEnd;
        int duration = 0;
        for (Interval interval : intervals) {
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

    /**
     * Inserts string in StringBuilder line.
     * @param sb StringBuilder instance
     * @param pos Position to insert
     * @param word String to insert
     */
    protected void insertInLine(final StringBuilder sb, final int pos,
                              final String word) {
        sb.replace(pos, pos + word.length(), word);
    }

    /**
     * Container class to represents an interval.
     */
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
