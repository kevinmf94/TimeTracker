package cat.uab.ds.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import paginaweb.PaginaWeb;

/**
 * Visitor in charge of read an Activity (Task or Project) and print it in
 * console.
 */
public class DetailedReportHTML extends DetailedReportVisitor {

    private PaginaWeb web = new PaginaWeb();

    public static final int TITLE_SIZE = 1;
    public static final int SECTION_HEADER_SIZE = 2;

    public static final int FILE_1 = 1;
    public static final int FILE_2 = 2;
    public static final int FILE_3 = 3;
    public static final int FILE_4 = 4;

    public static final int COL_1 = 1;
    public static final int COL_2 = 2;
    public static final int COL_3 = 3;
    public static final int COL_4 = 4;
    public static final int COL_5 = 5;
    public static final int COL_6 = 6;

    /**
     * Initialize basic menu info with table header.
     * @param newStartDate Start period
     * @param newEndDate End period
     */
    public DetailedReportHTML(final Date newStartDate, final Date newEndDate) {
        super(newStartDate, newEndDate);
    }

    @Override
    protected final void headersReport() {

        web.afegeixLiniaSeparacio();
        web.afegeixHeader("Detailed Report", TITLE_SIZE, true);
        web.afegeixLiniaSeparacio();

        //Period data
        web.afegeixHeader("Period", SECTION_HEADER_SIZE, false);

        Taula taula = new Taula(FILE_4, COL_2);
        taula.setPosicio(FILE_1, COL_1, "");
        taula.setPosicio(FILE_1, COL_2, "Date");
        taula.setPosicio(FILE_2, COL_1, "From");
        taula.setPosicio(FILE_2, COL_2, getDateString(getStartDate()));
        taula.setPosicio(FILE_3, COL_1, "To");
        taula.setPosicio(FILE_3, COL_2, getDateString(getEndDate()));
        taula.setPosicio(FILE_4, COL_1, "Date from generation of report");
        taula.setPosicio(FILE_4, COL_2, getDateString(new Date()));

        web.afegeixTaula(taula.getTaula(), true, true);
        web.afegeixLiniaSeparacio();
    }

    @Override
    protected final void projectReport() {
        Collection<String> items = getProjectsResults();

        web.afegeixHeader("Root projects", SECTION_HEADER_SIZE, false);

        Taula table = new Taula(FILE_1 + items.size(), COL_4);
        //Header table
        table.setPosicio(FILE_1, COL_1, "Project");
        table.setPosicio(FILE_1, COL_2, "Start date");
        table.setPosicio(FILE_1, COL_3, "End date");
        table.setPosicio(FILE_1, COL_4, "Total time");
        setTableData(table, items);

        web.afegeixTaula(table.getTaula(), true, false);
        web.afegeixLiniaSeparacio();
    }

    @Override
    protected final void subProjectsReport() {
        Collection<String> items = getSubProjectsResults();

        web.afegeixHeader("Sub projects", SECTION_HEADER_SIZE, false);

        Taula table = new Taula(FILE_1 + items.size(), COL_4);
        //Header table
        table.setPosicio(FILE_1, COL_1, "Project");
        table.setPosicio(FILE_1, COL_2, "Start date");
        table.setPosicio(FILE_1, COL_3, "End date");
        table.setPosicio(FILE_1, COL_4, "Total time");
        setTableData(table, items);

        web.afegeixTaula(table.getTaula(), true, false);
        web.afegeixLiniaSeparacio();
    }

    @Override
    protected final void taskReport() {
        Collection<String> items = getTasksResults();

        web.afegeixHeader("Tasks", SECTION_HEADER_SIZE, false);

        Taula table = new Taula(FILE_1 + items.size(), COL_5);
        //Header table
        table.setPosicio(FILE_1, COL_1, "No. Project");
        table.setPosicio(FILE_1, COL_2, "Task");
        table.setPosicio(FILE_1, COL_3, "Start date");
        table.setPosicio(FILE_1, COL_4, "End date");
        table.setPosicio(FILE_1, COL_5, "Total time");
        setTableData(table, items);

        web.afegeixTaula(table.getTaula(), true, false);
        web.afegeixLiniaSeparacio();
    }

    @Override
    protected final void intervalsReport() {
        Collection<String> items = getIntervalsResults();

        web.afegeixHeader("Intervals", SECTION_HEADER_SIZE, false);

        Taula table = new Taula(FILE_1 + items.size(), COL_6);
        //Header table
        table.setPosicio(FILE_1, COL_1, "No. Project");
        table.setPosicio(FILE_1, COL_2, "Task");
        table.setPosicio(FILE_1, COL_3, "Interval");
        table.setPosicio(FILE_1, COL_4, "Start date");
        table.setPosicio(FILE_1, COL_5, "End date");
        table.setPosicio(FILE_1, COL_6, "Total time");
        setTableData(table, items);

        web.afegeixTaula(table.getTaula(), true, false);
        web.afegeixLiniaSeparacio();
    }

    private void setTableData(final Taula table,
                             final Collection<String> results) {
        String tmp;
        String[] tmpItems;
        ArrayList<String> items = (ArrayList<String>) results;

        for (int i = 0; i < results.size(); i++) {
            tmp = items.get(i);
            tmpItems = tmp.split("\\" + SEPARATOR);
            for (int j = 0; j < tmpItems.length; j++) {
                table.setPosicio(i + 2, j + 1, tmpItems[j]);
            }
        }
    }

    public final String getResult() {
        headersReport();
        projectReport();
        subProjectsReport();
        taskReport();
        intervalsReport();
        web.escriuPagina();
        return "";
    }
}
