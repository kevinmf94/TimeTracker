package cat.uab.ds.core.utils;

import java.util.Date;

/**
 * Visitor in charge of read an Activity (Task or Project) and print it in
 * console.
 */
public class ShortReportAscii extends ShortReportVisitor {

    private StringBuilder result;

    // Array positions
    private static final int POS_0 = 0;
    private static final int POS_1 = 1;
    private static final int POS_2 = 2;
    private static final int POS_3 = 3;

    /**
     * Initialize basic menu info with table header.
     * @param newStartDate Start period
     * @param newEndDate End period
     */
    public ShortReportAscii(final Date newStartDate, final Date newEndDate) {
        super(newStartDate, newEndDate);

        this.result = new StringBuilder();
    }

    /**
     * Add report header to result.
     */
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

    /**
     * Add root projects report to result.
     */
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

        String[] lineTmp;

        for (String line : getProjectsResults()) {
            lineTmp = line.split("\\" + SEPARATOR);
            StringBuilder sbLine = new StringBuilder(WHITE_LINE);
            insertInLine(sbLine, POS_PROJECT_NAME, lineTmp[POS_0]);
            insertInLine(sbLine, POS_PROJECT_START, lineTmp[POS_1]);
            insertInLine(sbLine, POS_PROJECT_END, lineTmp[POS_2]);
            insertInLine(sbLine, POS_PROJECT_DURATION, lineTmp[POS_3]);
            result.append("\n").append(sbLine.toString());
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
     * Return the final report.
     * @return
     */
    @Override
    public final String getResult() {
        headersReport();
        projectReport();
        return result.toString();
    }
}
