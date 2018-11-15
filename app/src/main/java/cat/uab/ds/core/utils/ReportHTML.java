package cat.uab.ds.core.utils;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import paginaweb.PaginaWeb;

public class ReportHTML implements ReportFormat {

    private PaginaWeb web = new PaginaWeb();

    private static final int TITLE_SIZE = 1;
    private static final int SECTION_HEADER_SIZE = 2;

    private static final int ROW_1 = 1;
    private static final int ROW_2 = 2;
    private static final int ROW_3 = 3;
    private static final int ROW_4 = 4;

    private static final int COL_1 = 1;
    private static final int COL_2 = 2;

    // Date Time Format
    private static final SimpleDateFormat DF =
            new SimpleDateFormat("dd/MM/YYYY, HH:mm:ss",
                    new Locale("en"));

    @Override
    public final void addLine() {
        web.afegeixLiniaSeparacio();
    }

    @Override
    public final void newLine() {

    }

    @Override
    public final void addHeader(final String name, final Date start,
                                final Date end) {
        addLine();
        web.afegeixHeader(name, TITLE_SIZE, true);
        addLine();

        //Period data
        web.afegeixHeader("Period", SECTION_HEADER_SIZE, false);

        Taula taula = new Taula(ROW_4, COL_2);
        taula.setPosicio(ROW_1, COL_1, "");
        taula.setPosicio(ROW_1, COL_2, "Date");
        taula.setPosicio(ROW_2, COL_1, "From");
        taula.setPosicio(ROW_2, COL_2, DF.format(start));
        taula.setPosicio(ROW_3, COL_1, "To");
        taula.setPosicio(ROW_3, COL_2, DF.format(end));
        taula.setPosicio(ROW_4, COL_1, "Date from generation of report");
        taula.setPosicio(ROW_4, COL_2, DF.format(new Date()));

        web.afegeixTaula(taula.getTaula(), true, true);
    }

    @Override
    public final void addTable(final String[] header,
                        final Collection<String[]> rows,
                        final int[] columnsSizes) {
        Taula table = new Taula(rows.size() + 1, header.length);

        //Header table
        for (int i = 0; i < header.length; i++) {
            table.setPosicio(ROW_1, i + 1, header[i]);
        }
        int i = 2;
        for (String[] row : rows) {
            for (int j = 0; j < header.length; j++) {
                table.setPosicio(i, j + 1, row[j]);
            }
            i++;
        }

        web.afegeixTaula(table.getTaula(), true, false);
    }

    @Override
    public final void addText(final String text) {
        web.afegeixHeader(text, SECTION_HEADER_SIZE, false);
    }

    @Override
    public final void generate() {
        web.escriuPagina();
    }
}