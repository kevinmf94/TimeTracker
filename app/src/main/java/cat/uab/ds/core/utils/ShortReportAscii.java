package cat.uab.ds.core.utils;

import java.util.Collection;
import java.util.Date;

/**
 * Visitor in charge of read an Activity (Task or Project) and print it in
 * console.
 */
public class ShortReportAscii extends ShortReportVisitor {

    private StringBuilder result;

    /**
     * Initialize basic menu info with table header.
     * @param newStartDate Start period
     * @param newEndDate End period
     */
    public ShortReportAscii(final Date newStartDate, final Date newEndDate) {
        super(newStartDate, newEndDate);

        this.result = new StringBuilder();
    }

    @Override
    protected final void headersReport() {
        writeLine();
        result.append("\nShort report");
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

        StringBuilder sb = new StringBuilder(WHITE_LINE);
        insertInLine(sb, 0, "Project");
        insertInLine(sb, POS_PROJECT_START, "Start Date");
        insertInLine(sb, POS_PROJECT_END, "End Date");
        insertInLine(sb, POS_PROJECT_DURATION, "Total time");

        result.append("\n" + sb.toString());

        printResults(getProjectsResults());
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

    @Override
    public final String getResult() {
        headersReport();
        projectReport();
        return result.toString();
    }
}
