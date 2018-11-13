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
        result.append("\nTo ").append(getDateString(getEndDate()));
        result.append("\nDate from generation of report ")
                .append(getDateString(new Date()));

    }

    @Override
    protected final void projectReport() {
        writeLine();
        result.append("\nRoot projects");
        result.append("\nNo. Project Start Date End Date Total time");
        printResults(getProjectsResults());
    }

    @Override
    protected final void subProjectsReport() {
        writeLine();
        result.append("\nSub projects");
        result.append("\nNo. Project Start Date End Date Total time");
        printResults(getSubProjectsResults());
    }

    @Override
    protected final void taskReport() {
        writeLine();
        result.append("\nTasks");
        result.append("\nNo.Project Task Start Date End Date Total time");
        printResults(getTasksResults());
    }

    @Override
    protected final void intervalsReport() {
        writeLine();
        result.append("\nIntervals");
        result.append("\nNo.Project Task Interval "
                + "Start Date End Date Total time");
        printResults(getIntervalsResults());
    }

    private void printResults(final Collection<String> results) {
        for (String item: results) {
            item = item.replace("|", " ");
            result.append("\n").append(item);
        }
    }

    private void writeLine() {
        result.append("\n------------------------------"
                + "---------------------"
                + "-------------------------");
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
