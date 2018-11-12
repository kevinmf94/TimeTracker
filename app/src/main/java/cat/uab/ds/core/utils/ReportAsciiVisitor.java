package cat.uab.ds.core.utils;

import java.util.Date;

public abstract class ReportAsciiVisitor extends ReportVisitor {

    private StringBuilder activeStringBuilder = null;

    /**
     * Initialize basic menu info with table header.
     * @param newStartDate Start period
     * @param newEndDate End period
     */
    public ReportAsciiVisitor(final Date newStartDate, final Date newEndDate) {
        super(newStartDate, newEndDate);
    }

    protected final void writeLine() {
        activeStringBuilder.append("\n------------------------------"
                + "---------------------"
                + "-------------------------");
    }

    protected final void writeTxt(final String string) {
        activeStringBuilder.append("\n" + string);
    }

    public final StringBuilder getActiveStringBuilder() {
        return activeStringBuilder;
    }

    public final void setActiveStringBuilder(
            final StringBuilder newActiveStringBuilder) {
        this.activeStringBuilder = newActiveStringBuilder;
    }

}
