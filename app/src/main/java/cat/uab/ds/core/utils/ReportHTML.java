package cat.uab.ds.core.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import paginaweb.PaginaWeb;

public class ReportHTML implements ReportFormat {

    private PaginaWeb web = new PaginaWeb();

    private static final int TITLE_SIZE = 1;
    private static final int SECTION_HEADER_SIZE = 2;

    private static final int FILE_1 = 1;
    private static final int FILE_2 = 2;
    private static final int FILE_3 = 3;
    private static final int FILE_4 = 4;

    private static final int COL_1 = 1;
    private static final int COL_2 = 2;

    // Date Time Format
    private static final SimpleDateFormat DF =
            new SimpleDateFormat("dd/MM/YYYY, HH:mm:ss",
                    new Locale("en"));

    @Override
    public void addLine() {
        web.afegeixLiniaSeparacio();
    }

    @Override
    public void newLine() {

    }

    @Override
    public void addHeader(final String name, Date start, Date end) {
        addLine();
        web.afegeixHeader(name, TITLE_SIZE, true);
        addLine();

        //Period data
        web.afegeixHeader("Period", SECTION_HEADER_SIZE, false);

        Taula taula = new Taula(FILE_4, COL_2);
        taula.setPosicio(FILE_1, COL_1, "");
        taula.setPosicio(FILE_1, COL_2, "Date");
        taula.setPosicio(FILE_2, COL_1, "From");
        taula.setPosicio(FILE_2, COL_2, DF.format(start));
        taula.setPosicio(FILE_3, COL_1, "To");
        taula.setPosicio(FILE_3, COL_2, DF.format(end));
        taula.setPosicio(FILE_4, COL_1, "Date from generation of report");
        taula.setPosicio(FILE_4, COL_2, DF.format(new Date()));

        web.afegeixTaula(taula.getTaula(), true, true);
    }

    @Override
    public void addTable(String[] header, Collection<String[]> rows, int[] columnsSizes) {
        Taula table = new Taula(rows.size() + 1, header.length);

        //Header table
        for (int i = 0; i < header.length; i++) {
            table.setPosicio(FILE_1, i + 1, header[i]);
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
    public void addText(String text) {
        web.afegeixHeader(text, SECTION_HEADER_SIZE, false);
    }

    @Override
    public void generate() {
        web.escriuPagina();
    }
}
