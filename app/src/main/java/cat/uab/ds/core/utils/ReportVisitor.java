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
     * If interval is out of range, return null.
     * @param interval IntervalEntity
     * @return ReportInterval, the container with Start Date, End Date and
     * the duration
     */
    public final ReportInterval convertToReportInterval(
            final Interval interval) {
        Date start = interval.getStart();
        Date end = interval.getEnd();

        if ((start.compareTo(startDate) < 0 && end.compareTo(startDate) < 0)
                || (start.compareTo(endDate) > 0 && end.compareTo(endDate) > 0)
            ) { // Interval out of range
            return null;
        }

        if (start.before(startDate)) { //Left overflow
            start = startDate;
        }

        if (end.after(endDate)) { //Right overflow
            end = endDate;
        }

        if (start.equals(end)) {
            return null;
        }

        return new ReportInterval(start, end,
                Interval.getDuration(start, end));
    }

    /**
     * Merge a set of ReportInterval.
     * @param intervals Set of ReportInterval
     * @return ReportInterval with minStart, maxEnd and durationSum
     */
    public final ReportInterval mergeReportInterval(
            final Collection<ReportInterval> intervals) {

        Date minStart = null, maxEnd = null;
        int durationSum = 0;

        for (ReportInterval interval : intervals) {
            durationSum += interval.getDuration();

            if (minStart == null
                    || interval.getStart().compareTo(minStart) < 0) {
                minStart = interval.getStart();
            }
            if (maxEnd == null
                    || interval.getEnd().compareTo(maxEnd) > 0) {
                maxEnd = interval.getEnd();
            }
        }

        return new ReportInterval(minStart, maxEnd, durationSum);
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

    public final ReportFormat getReportFormat() {
        return reportFormat;
    }

    public final void setReportFormat(final ReportFormat newReportFormat) {
        this.reportFormat = newReportFormat;
    }
}
