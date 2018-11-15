package cat.uab.ds.core.utils;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

public class ReportAscii implements  ReportFormat {

    static final String WHITE_LINE = "                                     "
            + "                                           ";
    private StringBuilder result = new StringBuilder();

    // Date Time Format.
    private static final SimpleDateFormat DF =
            new SimpleDateFormat("dd/MM/YYYY, HH:mm:ss",
                    new Locale("en"));

    @Override
    public void newLine() {
        result.append("\n");
    }

    @Override
    public void addLine() {
        result.append("------------------------------"
                + "---------------------"
                + "-------------------------");
        newLine();
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

    @Override
    public void addHeader(final String name, final Date start, final Date end) {
        addLine();
        addText(name);
        addLine();
        result.append("Period");
        newLine();
        result.append("Date");
        newLine();
        result.append("From ").append(DF.format(start));
        newLine();
        result.append("To   ").append(DF.format(end));
        newLine();
        result.append("Date from generation of report ")
                .append(DF.format(new Date()));
        newLine();
    }

    @Override
    public void addTable(final String[] header, final Collection<String[]> rows,
                         final int[] columnsSizes) {

        StringBuilder headerStr = new StringBuilder(WHITE_LINE);
        for (int i = 0; i < header.length; i++) {
            insertInLine(headerStr, columnsSizes[i], header[i]);
        }
        result.append(headerStr.toString());
        newLine();

        for (String[] r : rows) {
            StringBuilder rowStr = new StringBuilder(WHITE_LINE);
            for (int i = 0; i < header.length; i++) {
                insertInLine(rowStr, columnsSizes[i], r[i]);
            }
            result.append(rowStr.toString());
            newLine();
        }
    }

    @Override
    public void addText(final String text) {
        result.append(text);
        newLine();
    }

    @Override
    public void generate() {
        System.out.println(result);
    }
}
