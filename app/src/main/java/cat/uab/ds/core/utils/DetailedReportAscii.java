package cat.uab.ds.core.utils;

import java.util.Date;

/**
 * Visitor in charge of read an Activity (Task or Project) and print it in
 * console.
 */
public class DetailedReportAscii extends DetailedReportVisitor {

    private static final int POS_TASK_PROJECT = 0;
    private static final int POS_TASK_NAME = 8;
    private static final int POS_TASK_START = 15;
    private static final int POS_TASK_END = 38;
    private static final int POS_TASK_DURATION = 60;

    private static final int POS_INTERVAL_PROJECT = 0;
    private static final int POS_INTERVAL_TASK = 8;
    private static final int POS_INTERVAL_NAME = 15;
    private static final int POS_INTERVAL_START = 25;
    private static final int POS_INTERVAL_END = 48;
    private static final int POS_INTERVAL_DURATION = 70;

    private static final int POS_0 = 0;
    private static final int POS_1 = 1;
    private static final int POS_2 = 2;
    private static final int POS_3 = 3;
    private static final int POS_4 = 4;
    private static final int POS_5 = 5;

    private StringBuilder result;

    /**
     * Initialize basic menu info with table header.
     * @param newStartDate Start period
     * @param newEndDate End period
     */
    public DetailedReportAscii(final Date newStartDate, final Date newEndDate) {
        super(newStartDate, newEndDate, null);

        this.result = new StringBuilder();
    }

    /**
     * Add header's report to result.
     */
    @Override
    protected final void headersReport() {
        writeLine();
        result.append("\nDetailed report");
        writeLine();
        result.append("\nPeriod");
        result.append("\nDate");
        result.append("\nFrom ").append(getDateString(getStartDate()));
        result.append("\nTo   ").append(getDateString(getEndDate()));
        result.append("\nDate from generation of report ")
                .append(getDateString(new Date()));

    }

    /**
     * Add root project's report.
     */
    @Override
    protected final void projectReport() {
        writeLine();
        result.append("\nRoot projects");
        projectsHeader();
        String[] lineTmp;

        for (String line : getProjectsResults()) {
            lineTmp = line.split("\\" + SEPARATOR);
            StringBuilder sb = new StringBuilder(WHITE_LINE);
            insertInLine(sb, POS_PROJECT_NAME, lineTmp[POS_0]);
            insertInLine(sb, POS_PROJECT_START, lineTmp[POS_1]);
            insertInLine(sb, POS_PROJECT_END, lineTmp[POS_2]);
            insertInLine(sb, POS_PROJECT_DURATION, lineTmp[POS_3]);
            result.append("\n").append(sb.toString());
        }
    }

    /**
     * Add subprojects report's to result.
     */
    @Override
    protected final void subProjectsReport() {
        writeLine();
        result.append("\nSub projects");
        projectsHeader();

        String[] lineTmp;

        for (String line : getSubProjectsResults()) {
            lineTmp = line.split("\\" + SEPARATOR);
            StringBuilder sb = new StringBuilder(WHITE_LINE);
            insertInLine(sb, POS_PROJECT_NAME, lineTmp[POS_0]);
            insertInLine(sb, POS_PROJECT_START, lineTmp[POS_1]);
            insertInLine(sb, POS_PROJECT_END, lineTmp[POS_2]);
            insertInLine(sb, POS_PROJECT_DURATION, lineTmp[POS_3]);
            result.append("\n").append(sb.toString());
        }
    }

    /**
     * Add task's report to result.
     */
    @Override
    protected final void taskReport() {
        writeLine();
        result.append("\nTasks");
        tasksHeader();

        String[] lineTmp;

        for (String line : getTasksResults()) {
            lineTmp = line.split("\\" + SEPARATOR);

            StringBuilder sb = new StringBuilder(WHITE_LINE);
            insertInLine(sb, POS_TASK_PROJECT, lineTmp[POS_0]);
            insertInLine(sb, POS_TASK_NAME, lineTmp[POS_1]);
            insertInLine(sb, POS_TASK_START, lineTmp[POS_2]);
            insertInLine(sb, POS_TASK_END, lineTmp[POS_3]);
            insertInLine(sb, POS_TASK_DURATION, lineTmp[POS_4]);
            result.append("\n").append(sb.toString());
        }
    }

    /**
     * Add intervals' report to result.
     */
    @Override
    protected final void intervalsReport() {
        writeLine();
        result.append("\nIntervals");
        intervalsHeader();

        String[] lineTmp;

        for (String line : getIntervalsResults()) {
            lineTmp = line.split("\\" + SEPARATOR);

            StringBuilder sb = new StringBuilder(WHITE_LINE);
            insertInLine(sb, POS_INTERVAL_PROJECT, lineTmp[POS_0]);
            insertInLine(sb, POS_INTERVAL_TASK, lineTmp[POS_1]);
            insertInLine(sb, POS_INTERVAL_NAME, lineTmp[POS_2]);
            insertInLine(sb, POS_INTERVAL_START, lineTmp[POS_3]);
            insertInLine(sb, POS_INTERVAL_END, lineTmp[POS_4]);
            insertInLine(sb, POS_INTERVAL_DURATION, lineTmp[POS_5]);
            result.append("\n").append(sb.toString());
        }
    }

    /**
     * Add line separator to result.
     */
    private void writeLine() {
        result.append("\n------------------------------"
                + "---------------------"
                + "-------------------------");
    }
    /**
     * Add projects header to result.
     */
    private void projectsHeader() {
        StringBuilder sb = new StringBuilder(WHITE_LINE);
        insertInLine(sb, POS_PROJECT_NAME, "Project");
        insertInLine(sb, POS_PROJECT_START, "Start Date");
        insertInLine(sb, POS_PROJECT_END, "End Date");
        insertInLine(sb, POS_PROJECT_DURATION, "Total time");
        result.append("\n" + sb.toString());
    }
    /**
     * Add tasks header to result.
     */
    private void tasksHeader() {
        StringBuilder sb = new StringBuilder(WHITE_LINE);
        insertInLine(sb, POS_TASK_PROJECT, "Project");
        insertInLine(sb, POS_TASK_NAME, "Task");
        insertInLine(sb, POS_TASK_START, "Start Date");
        insertInLine(sb, POS_TASK_END, "End Date");
        insertInLine(sb, POS_TASK_DURATION, "Total time");
        result.append("\n" + sb.toString());
    }
    /**
     * Add intervals header to result.
     */
    private void intervalsHeader() {
        StringBuilder sb = new StringBuilder(WHITE_LINE);
        insertInLine(sb, POS_INTERVAL_PROJECT, "Project");
        insertInLine(sb, POS_INTERVAL_TASK, "Task");
        insertInLine(sb, POS_INTERVAL_NAME, "Interval");
        insertInLine(sb, POS_INTERVAL_START, "Start Date");
        insertInLine(sb, POS_INTERVAL_END, "End Date");
        insertInLine(sb, POS_INTERVAL_DURATION, "Total time");
        result.append("\n" + sb.toString());
    }

    /**
     * Return the final result.
     * @return
     */
    public final String getResult() {
        headersReport();
        projectReport();
        subProjectsReport();
        taskReport();
        intervalsReport();
        return result.toString();
    }
}
