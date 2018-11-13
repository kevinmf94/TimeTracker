package cat.uab.ds.core.utils;

import java.util.Collection;
import java.util.Date;

/**
 * Visitor in charge of read an Activity (Task or Project) and print it in
 * console.
 */
public class DetailedReportAscii extends DetailedReportVisitor {

    private StringBuilder result;

    /**
     * Initialize basic menu info with table header.
     * @param newStartDate Start period
     * @param newEndDate End period
     */
    public DetailedReportAscii(final Date newStartDate, final Date newEndDate) {
        super(newStartDate, newEndDate);

        this.result = new StringBuilder();
    }

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

    @Override
    protected final void projectReport() {
        writeLine();
        result.append("\nRoot projects");
        projectsHeader();
        printResults(getProjectsResults());
    }

    @Override
    protected final void subProjectsReport() {
        writeLine();
        result.append("\nSub projects");
        projectsHeader();
        printResults(getSubProjectsResults());
    }

    @Override
    protected final void taskReport() {
        writeLine();
        result.append("\nTasks");
        tasksHeader();
        printResults(getTasksResults());
    }

    @Override
    protected final void intervalsReport() {
        writeLine();
        result.append("\nIntervals");
        intervalsHeader();
        printResults(getIntervalsResults());
    }

    private void printResults(final Collection<String> results) {
        for (String item: results) {
            result.append("\n").append(item);
        }
    }

    private void writeLine() {
        result.append("\n------------------------------"
                + "---------------------"
                + "-------------------------");
    }

    private void projectsHeader() {
        StringBuilder sb = new StringBuilder(WHITE_LINE);
        insertInLine(sb, POS_PROJECT_NAME, "Project");
        insertInLine(sb, POS_PROJECT_START, "Start Date");
        insertInLine(sb, POS_PROJECT_END, "End Date");
        insertInLine(sb, POS_PROJECT_DURATION, "Total time");
        result.append("\n" + sb.toString());
    }

    private void tasksHeader() {
        StringBuilder sb = new StringBuilder(WHITE_LINE);
        insertInLine(sb, POS_TASK_PROJECT, "Project");
        insertInLine(sb, POS_TASK_NAME, "Task");
        insertInLine(sb, POS_TASK_START, "Start Date");
        insertInLine(sb, POS_TASK_END, "End Date");
        insertInLine(sb, POS_TASK_DURATION, "Total time");
        result.append("\n" + sb.toString());
    }

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

    @Override
    public final String getResult() {
        headersReport();
        projectReport();
        subProjectsReport();
        taskReport();
        intervalsReport();
        return result.toString();
    }
}
